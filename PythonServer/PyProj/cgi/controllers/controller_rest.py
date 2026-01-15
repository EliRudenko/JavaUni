import datetime
import json
import sys

from models.request import CgiRequest


class RestStatus:
    def __init__(self,is_ok:bool, code:int, message:str):
        self.is_ok = is_ok
        self.code = code
        self.message = message

    def to_json(self):
        return {
            "isOk": self.is_ok,
            "code": self.code,
            "message": self.message
        }

RestStatus.status200 = RestStatus(True, 200, "OK")
RestStatus.status401 = RestStatus(False, 401, "Unauthorized")
RestStatus.status405 = RestStatus(False, 405, "Method Not Allowed")

class RestCache:
    def __init__(self, exp:str|int|None=None, lifetime:int|None=None):
        self.exp = exp
        self.lifetime = lifetime

    def to_json(self):
        return {
            "exp": self.exp,
            "lifetime": self.lifetime,
            "units": "seconds"
        }

RestCache.no = RestCache()
RestCache.hrs1 = RestCache(lifetime=60*60)

class RestMeta:
    def __init__(self, service:str, request_method:str|None=None, auth_user_id:str|int|None=None, data_type:str="null",
                 cache:RestCache=RestCache.no, server_time:int|None=None, params:dict|None=None, links:dict|None=None):
        self.service = service
        self.request_method = request_method
        self.auth_user_id = auth_user_id
        self.data_type = data_type
        self.cache = cache
        self.server_time = server_time if server_time is not None else datetime.datetime.now().timestamp()
        self.params = params
        self.links = links

    def to_json(self):
        return {
            "service": self.service,
            "request_method": self.request_method,
            "auth_user_id": self.auth_user_id,
            "data_type": self.data_type,
            "cache": self.cache.to_json(),
            "server_time": self.server_time,
            "params": self.params,
            "links": self.links,
        }

class RestResponse:
    def __init__(self, meta:RestMeta|None=None,
                  status:RestStatus=RestStatus.status200, data:any=None):
        self.status = status
        self.meta = meta
        self.data=data

    def to_json(self):
        return {
            "status": self.status.to_json(),
            "meta": self.meta.to_json(),
            "data": self.data,
        }
    



class RestController:

    def __init__(self, request:CgiRequest):
        self.request = request
        self.response = RestResponse()
        
    def serve(self):
        if self.response.meta is None:
            self.response.meta = RestMeta(
                service="Rest default service",
            )
        self.response.meta.request_method = self.request.request_method

        action = "do_" + self.request.request_method.lower()
        controller_action = getattr(self, action, None)
        if controller_action:
            controller_action()
        else:
            self.response.status = RestStatus.status405
        sys.stdout.buffer.write(b"Content-Type: application/json; charset=utf-8\n\n")
        sys.stdout.buffer.write(json.dumps(self.response, ensure_ascii=False,
                                            default=lambda x: x.to_json() if hasattr(x, 'to_json') else str).encode())
        
    def send_error(self, message: str, status_code: int):
        self.response.status = RestStatus(False, status_code, "Error")
        self.response.meta.cache = RestCache.no
        self.response.meta.data_type = "string"
        self.response.data = message
        
    def send_header_missing_response(self, header_name:str):
        self.response.status = RestStatus(False, 403, f"Forbidden: Missing required header '{header_name}'")
        self.response.meta.data_type = "null"
        self.response.data = None