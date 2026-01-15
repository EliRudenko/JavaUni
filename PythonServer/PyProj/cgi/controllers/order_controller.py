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
RestStatus.status405 = RestStatus(False, 405, "Method Not Allowed")
RestStatus.status201 = RestStatus(True, 201, "Created")
RestStatus.status204 = RestStatus(True, 204, "No Content")
RestStatus.status403 = RestStatus(False, 403, "Forbidden")
RestStatus.status404 = RestStatus(False, 404, "Not Found")


class RestCache:
    def __init__(self, exp:str|int|None=None, lifetime:int|None=None):
        self.exp = exp
        self.lifetime = lifetime

    def to_json(self):
        data = {
            "units": "seconds"
        }
        if self.exp is not None:
            data["exp"] = self.exp
        if self.lifetime is not None:
            data["lifetime"] = self.lifetime
        return data

RestCache.no = RestCache()
RestCache.hrs1 = RestCache(lifetime=60*60)

class RestMeta:
    def __init__(self, service:str, request_method:str, auth_user_id:str|int|None=None, data_type:str="null",
                 cache:RestCache=RestCache.no, server_time:int|None=None, params:dict|None=None, links:dict|None=None):
        self.service = service
        self.request_method = request_method
        self.auth_user_id = auth_user_id
        self.data_type = data_type
        self.cache = cache
        self.server_time = server_time if server_time is not None else int(datetime.datetime.now().timestamp() * 1000)
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
    def __init__(self, meta:RestMeta,
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

class OrderController:
    def __init__(self, request:CgiRequest):
        self.request = request
        self.response = None


    def _check_custom_header(self):
        header = self.request.headers.get("Custom-Header")
        
        if header is None or header.strip() == "":
            self.response.status = RestStatus.status403
            self.response.status.message = "Forbidden: Missing required header 'Custom-Header'"
            self.response.meta.data_type = "null"
            self.response.data = None
            return False
        return True

    def serve(self):
        self.response = RestResponse(meta=RestMeta(
            service="Order API",
            request_method=self.request.request_method,
            links={
                "get": "GET /order/{id}",
                "post": "POST /order",
                "put": "PUT /order/{id}",
                "delete": "DELETE /order/{id}",
            }
        ))

        action = "do_" + self.request.request_method.lower()
        controller_action = getattr(self, action, None)
        
        if controller_action:
            controller_action()
        else:
            self.response.status = RestStatus.status405
        
        http_status_line = f"Status: {self.response.status.code} {self.response.status.message}\n"
        sys.stdout.buffer.write(http_status_line.encode('utf-8'))
        
        sys.stdout.buffer.write(b"Content-Type: application/json; charset=utf-8\n\n")

        json_output = json.dumps(self.response, ensure_ascii=False,
                                 default=lambda x: x.to_json() if hasattr(x, 'to_json') else str)

        sys.stdout.buffer.write(json_output.encode('utf-8'))


    def do_get(self):
        self.response.meta.data_type = "object"
        self.response.meta.cache = RestCache.hrs1 
        self.response.data = {
            "order_id": 12345,
            "status": "shipped",
            "total_price": 499.99,
            "method": "GET"
        }

    def do_post(self):
        self.response.meta.data_type = "object"
        self.response.status = RestStatus.status201 
        self.response.data = {
            "order_id": 12346,
            "message": "Order successfully created",
            "method": "POST",
            "payload_received": getattr(self.request, 'data', {}) 
        }

    def do_put(self):
        self.response.meta.data_type = "object"
        self.response.data = {
            "order_id": 12345,
            "message": "Order successfully replaced (PUT)",
            "method": "PUT"
        }

    def do_patch(self):
        self.response.meta.data_type = "object"
        self.response.data = {
            "order_id": 12345,
            "message": "Order successfully updated (PATCH)",
            "method": "PATCH"
        }

    def do_delete(self):
        self.response.status = RestStatus.status204 
        self.response.data = None 
        self.response.meta.data_type = "null"