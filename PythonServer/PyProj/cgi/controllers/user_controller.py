import datetime
import base64, binascii, re
from dao import helper
from controllers.controller_rest import RestController, RestMeta, RestStatus, RestCache
from dao.data_accessor import DataAccessor

class UserController(RestController):

    def serve(self):
        self.response.meta = RestMeta(
           service="User API",
           links={
               "get": "GET /user",
               "post": "POST /user",
           }
        )
        super().serve()

    def do_get(self):
        self.response.meta.service += ": authentication"

        auth_header = self.request.headers.get("Authorization", None)
        if not auth_header:
            self.send_error("Unauthorized: Missing 'Authorization' header", 401)
            return
        
        auth_scheme = 'Basic '
        if not auth_header.startswith(auth_scheme):
            self.send_error(f"Unauthorized: Invalid 'Authorization' header format: {auth_scheme} only", 401)
            return
        
        credentials = auth_header[len(auth_scheme):]
        if len(credentials) < 7:
            self.send_error("Unauthorized: Short 'Authorization' credentials value", 401)
            return
        
        match = re.search(r"[^a-zA-Z0-9+/=]", credentials)
        if match:
            self.send_error(f"Unauthorized: Format error (invalid symbol) for credentials {credentials}", 401)
            return
        
        user_pass = None
        try:
            user_pass = base64.b64decode(credentials).decode('utf-8')
        except binascii.Error:
            self.send_error(f"Unauthorized: Padding error for credentials {credentials}", 401)
            return
        except Exception:
            self.send_error(f"Unauthorized: Decoding error for credentials {credentials}", 401)
            return
        
        if not user_pass:
            self.send_error(f"Unauthorized: Decode error for credentials {credentials}", 401)
            return

        if not ':' in user_pass:
            self.send_error("Unauthorized: Credential format error", 401)
            return
        
        login, password = user_pass.split(':', 1)
        data_accessor = DataAccessor()
        user = data_accessor.authenticate(login, password)
        if not user:
            self.send_error("Unauthorized: Credentials rejected", 401)
            return
        payload = {
            "sub": str(user['user_id']),
            "iss": "KNP_221_CGI",
            "aud": user['role_id'],
            "iat": datetime.datetime.now().timestamp(),
            "name": user['user_name'],
            "email": user['user_email'],
        }
        self.response.meta.cache = RestCache.hrs1
        self.response.meta.data_type = "token"
        self.response.data = helper.compose_jwt(payload)


    def do_post(self):
        self.response.meta.service += ": registration"
        self.response.meta.data_type = "object"
        self.response.data = {
            "int": 10,
            "float": 1e-3,
            "str": "POST",
            "cyr": "Привіт",
            "headers": self.request.headers,
        }

    