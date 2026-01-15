import datetime
import hashlib,hmac,random,string,base64,json, re, binascii
import uuid

from models.request import CgiRequest


def jwt_payload_from_request(request:CgiRequest, required: bool=False) -> dict|None:

    try:
        payload = get_payload_from_jwt(get_bearer(request))
        validate_jwt_time(payload)
    except ValueError as e:
        if required:
            raise ValueError(str(e))
        return None
    else:
        return payload
    

def validate_jwt_time(payload:dict, max_time:int=1000) -> None:
    if not ("iat" in payload or "nbf" in payload or "exp" in payload):
        raise ValueError("Token must include 'iat' and at least one of 'nbf' or 'exp'")
    now = datetime.datetime.now().timestamp()
    if "nbf" in payload and payload["nbf"] > now:
        raise ValueError("Token is not yet valid (nbf violation)")
    if "exp" in payload and payload["exp"] < now:
        raise ValueError("Token is expired (exp violation)")
    if not "exp" in payload:
        if not ("iat" in payload or "nbf" in payload):
            raise ValueError("Origin time field(nbf or iat) required if nbf is missing")
        
        start_time = max(payload.get("iat", 0), payload.get("nbf", 0))
            
        if now - start_time > max_time:
            raise ValueError("Max validity time exceeded")

def validate_jwt_claims(payload: dict, issuer: str = "Server-KN-P-221") -> None:
    if "sub" not in payload:
        raise ValueError("Token must include 'sub' claim")

    if not isinstance(payload["sub"], str):
        raise ValueError("'sub' must be a string")

    try:
        uuid.UUID(payload["sub"])
    except ValueError:
        raise ValueError("'sub' must be a valid UUID")

    if payload.get("iss") != issuer:
        raise ValueError(f"Invalid issuer (iss must be '{issuer}')")

    if not ("name" in payload or "email" in payload):
        raise ValueError("Token must include at least one of: 'name' or 'email'")


def get_bearer(request:CgiRequest) -> str:
    auth_header = request.headers.get("Authorization", None)
    if not auth_header:
        raise ValueError("Unauthorized: Missing 'Authorization' header")
    
    auth_scheme = 'Bearer '
    if not auth_header.startswith(auth_scheme):
        raise ValueError(f"Unauthorized: Invalid 'Authorization' header format: {auth_scheme} only")
    
    return auth_header[len(auth_scheme):]


def get_payload_from_jwt(jwt:str) -> dict:
    if '.' not in jwt:
        raise ValueError("Invalid token format (missing parts separator)")
    
    jwtSplitted = jwt.split('.')
    
    non64char = re.compile(r'[^a-zA-Z0-9\-\_=]')
    if re.search(non64char, jwtSplitted[0]) != None:
        raise ValueError(f"Invalid token header format (invalid non-base64 character) `{joseHeader}`")
    
    try:
        joseHeader = b64_to_obj(jwtSplitted[0])
    except ValueError as e:
        raise ValueError(f"Invalid token header {e}")
    
    if not "typ" in joseHeader:
        raise ValueError("Invalid token header data (missing 'typ' field)")
    if not joseHeader["typ"] in ("JWT",):
        raise ValueError("Invalid token header data (unsupported 'typ' field)")
    
    if not "alg" in joseHeader:
        raise ValueError("Invalid token header data (missing 'alg' field)")
    if not joseHeader["alg"] in ("HS256", "HS384", "HS512",):
        raise ValueError("Invalid token header data (unsupported 'alg ' field)")
    
    #checking signature 
    if len(jwtSplitted) != 3:
        raise ValueError("Invalid token format (invalid parts count)")
    signedPart = jwtSplitted[0] + '.' + jwtSplitted[1]
    testSignature = get_signature(signedPart.encode('utf-8'), alg=joseHeader["alg"])

    if testSignature != jwtSplitted[2]:
        raise ValueError("Unauthorized: Invalid token signature")
    #decoding payload
    try:
        payload = b64_to_obj(jwtSplitted[1])
    except ValueError:
        payload_raw = base64.urlsafe_b64decode(jwtSplitted[1]).decode("utf-8")

        if joseHeader.get("cty") == "JWT":
            return get_payload_from_jwt(payload_raw)

        raise ValueError("Invalid token payload")
    
    if joseHeader.get("cty") == "JWT":
        raise ValueError("Invalid nested JWT: payload must be JWT string")
    
    if "nbf" in payload and not isinstance(payload["nbf"], (int, float)):
        raise ValueError("Token is not yet valid (nbf must be a number)")

    if "iat" in payload and not isinstance(payload["iat"], (int, float)):
        raise ValueError("Token issue time is invalid (iat must be a number)")
    
    if "exp" in payload and not isinstance(payload["exp"], (int, float)):
        raise ValueError("Token expiration time is invalid (exp must be a number)")
    
    return payload


def b64_to_obj(input:str) -> dict:
        non64char = re.compile(r'[^a-zA-Z0-9\-\_=]')
        if re.search(non64char, input) != None:
            raise ValueError(f"invalid non-base64 character `{input}`")
        try:
            decoded_bytes = base64.urlsafe_b64decode(input)
        except binascii.Error:
            raise ValueError("base64 padding error")
        
        try:
            result = json.loads(decoded_bytes.decode('utf-8'))
        except UnicodeDecodeError:
            raise ValueError("UTF-8 decoding error")
        except Exception:
            raise ValueError("JSON decoding error")

        if not isinstance(result, dict):
            raise ValueError("not a JSON object")
        return result    


def compose_jwt(payload:dict, typ:str="JWT", header:dict|None = None, alg:str = "HS256", secret_key:bytes|None = None) -> str:
    header = {
        "alg": alg,
        "typ": typ
    }
    j_header = json.dumps(header)
    j_payload = json.dumps(payload)
    b_header = base64.urlsafe_b64encode(j_header.encode("utf-8"))
    b_payload = base64.urlsafe_b64encode(j_payload.encode("utf-8"))

    body = b'.'.join([b_header, b_payload])

    signature = get_signature(body, secret_key, alg)
    return b'.'.join([body, signature.encode("ascii")]).decode("ascii")

def get_signature(data:bytes, secret_key:bytes|None = None, alg:str = "HS256", form:str = "base64url") -> str:
    if secret_key is None:
        secret_key = "secret".encode()
    if alg == "HS256":
        mode = hashlib.sha256
    elif alg == "HS512":
        mode = hashlib.sha512
    elif alg == "HS384":
        mode = hashlib.sha384
    else:
        raise ValueError(f"get_signature error: Unsupported algorithm: {alg}")
    mac = hmac.new(secret_key, data, mode)
    if form == "base64url":
        return base64.urlsafe_b64encode(mac.digest()).decode('ascii')
    elif form == "base64std":
        return base64.standard_b64encode(mac.digest()).decode('ascii')
    elif form == "hex":
        return mac.digest().hex()
    else:
        raise ValueError(f"get_signature error: Unsupported form: {form}")
    
    

def generate_salt(length:int=16) -> str:
    symbols = string.ascii_letters + string.digits
    return ''.join(random.choice(symbols) for _ in range(length))

def main():
    print(get_signature(b"eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.eyJzdWIiOiAiZjczMzVjMmYtYmY1MS0xMWYwLTk1ZjctMDI1MGYyODgyYzAwIiwgImlzcyI6ICJLTlBfMjIxX0NHSSIsICJhdWQiOiAiYWRtaW4iLCAiaWF0IjogMTc2NjQ5ODcwNi4yMDk0NDksICJuYW1lIjogIkRlZmF1bHQgYWRtaW5pc3RyYXRvciIsICJlbWFpbCI6ICJjaGFuZ2UubWVAZmFrZS5uZXQifQ==.2ZvQquXJ_BGiqac-zAsftbt-bSQocsHp39UNaexPYWQ="))

if __name__ == "__main__":
    main()