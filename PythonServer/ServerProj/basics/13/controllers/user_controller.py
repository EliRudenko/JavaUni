from controllers.controller_rest import RestController, RestMeta, RestStatus, RestCache
import base64, binascii, re, datetime, helper
from views.dao.data_accessor import DataAccessor
class UserController(RestController):

    def serve(self):
    
        self.response.meta = RestMeta(
                service="User API",
                requestMethod=self.request.request_method,
                links={
                    "get": "GET /user",
                    "post": "POST /user",
                }
            )
        
        super().serve()


    def send_401(self, message:str) :
        self.response.status = RestStatus.status401
        self.response.meta.cache = RestCache.no
        self.response.meta.dataType = "string"
        self.response.data = message

    def do_get(self):
        self.response.meta.service += ": authentication"
        auth_header = self.request.headers.get('Authorization', None)
        if not auth_header :
            self.send_401("Missing required header 'Authorization'")
            return 

        auth_scheme = 'Basic'
        if not auth_header.startswith(auth_scheme) :
            self.send_401(f"Invalid Authorization scheme: {auth_scheme} only")
            return
        
        credentials = auth_header[len(auth_scheme):].strip()
        if len(credentials) < 7 :
            self.send_401(f"{auth_scheme} credentials too short")
            return
        
        match = re.search(r"[^a-zA-Z0-9+/=]", credentials)
        if match:
            self.send_401(f"Format error (invalid symbol) for credentials {credentials}")
            return
        

        user_pass = None
        try: 
            user_pass = base64.standard_b64decode(credentials).decode(encoding="utf-8")

        except binascii.Error :
            self.send_401(f"Padding error for credentials {credentials}")
            return
        except Exception:
            self.send_401(f"Decode error for credentials {credentials}")
            return

        if not user_pass :
            self.send_401(f"Decode error for credentials {credentials}")
            return
        
        if not ':' in user_pass :
            self.send_401(f"User-pass format error(missing ':') {user_pass}")
            return


        login, password = user_pass.split(':', 1)

        data_accessor = DataAccessor()
        user = data_accessor.authenticate(login, password)

        if not user :
            self.send_401(f"credentials rejected) {user_pass}")
            return


        self.response.meta.cache = RestCache.hrs1
        self.response.meta.dataType = "object"
        payload = {
            "sub": str(user['user_id']),
            "iss": "Server-KN-P-221",
            "aud": user['role_id'],
            "iat": datetime.datetime.now().timestamp(),
            "name": user['user_name'],
            "email": user['user_email'],
        }
        self.response.data = helper.compose_jwt(payload)
        


    def do_post(self):
        self.response.meta.service += ": registration"
        self.response.meta.cache = RestCache.hrs1
        self.response.meta.dataType = "object"
        self.response.data = {
            "int": 10,
            "float": 1e-3,
            "str": "POST",
            "cyr": "Вітання",
            "headers": self.request.headers
        }
    

    def index(self):
        print("Content-Type: text/html; charset=utf-8")
        print()
        print("<h1>User Controller — index()</h1>")
