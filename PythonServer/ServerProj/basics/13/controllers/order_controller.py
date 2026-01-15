from models.request import CgiRequest
import json, datetime


# ===== REST  =====

class RestStatus:
    def __init__(self, is_ok: bool, code: int, message: str):
        self.is_ok = is_ok
        self.code = code
        self.message = message

RestStatus.status200 = RestStatus(True, 200, "OK")
RestStatus.status201 = RestStatus(True, 201, "Created")
RestStatus.status204 = RestStatus(True, 204, "No Content")
RestStatus.status403 = RestStatus(False, 403, "Forbidden")
RestStatus.status405 = RestStatus(False, 405, "Method Not Allowed")
RestStatus.status500 = RestStatus(False, 500, "Internal Server Error")


class RestCache:
    def __init__(self, lifetime: int | None = None):
        self.lifetime = lifetime

    def to_json(self):
        return {
            "lifetime": self.lifetime,
            "units": "seconds"
        }

RestCache.no = RestCache()
RestCache.hrs1 = RestCache(60 * 60)


class RestMeta:
    def __init__(
        self,
        service: str,
        requestMethod: str,
        dataType: str = "null",
        cache: RestCache = RestCache.no,
        params: dict | None = None,
        links: dict | None = None
    ):
        self.service = service
        self.requestMethod = requestMethod
        self.dataType = dataType
        self.cache = cache
        self.serverTime = datetime.datetime.now().timestamp()
        self.params = params
        self.links = links


class RestResponse:
    def __init__(
        self,
        meta: RestMeta,
        status: RestStatus = RestStatus.status200,
        data: any = None
    ):
        self.status = status
        self.meta = meta
        self.data = data


# ===== ORDER CONTROLLER =====

class OrderController:

    def __init__(self, request: CgiRequest):
        self.request = request
        self.response: RestResponse | None = None

    def serve(self):
        try:
            # проверка кастомного заголовка
            if "My-Custom-Header" not in self.request.headers:
                self.response = RestResponse(
                    meta=RestMeta(
                        service="Order API",
                        requestMethod=self.request.request_method
                    ),
                    status=RestStatus.status403
                )
                return self._print()

            self.response = RestResponse(
                meta=RestMeta(
                    service="Order API",
                    requestMethod=self.request.request_method,
                    links={
                        "get": "GET /order",
                        "post": "POST /order",
                        "put": "PUT /order/{id}",
                        "patch": "PATCH /order/{id}",
                        "delete": "DELETE /order/{id}"
                    }
                )
            )

            action = "do_" + self.request.request_method.lower()
            handler = getattr(self, action, None)

            if handler:
                handler()
            else:
                self.response.status = RestStatus.status405

        except Exception as e:
            self.response = RestResponse(
                meta=RestMeta(
                    service="Order API",
                    requestMethod=self.request.request_method
                ),
                status=RestStatus.status500,
                data={"error": str(e)}
            )

        self._print()

    # ===== OUTPUT =====

    def _print(self):
        print("Content-Type: application/json; charset=utf-8")
        print()
        print(json.dumps(self.response, default=lambda o: o.__dict__, ensure_ascii=False))

    # ===== HTTP METHODS =====

    def do_get(self):
        self.response.meta.dataType = "array"
        self.response.meta.cache = RestCache.hrs1
        self.response.data = [
            {"id": 1, "price": 1200, "status": "new"},
            {"id": 2, "price": 5400, "status": "paid"}
        ]

    def do_post(self):
        self.response.status = RestStatus.status201
        self.response.meta.dataType = "object"
        self.response.data = {
            "id": 3,
            "status": "created"
        }

    def do_put(self):
        self.response.meta.dataType = "object"
        self.response.data = {
            "message": "Order fully updated"
        }

    def do_patch(self):
        self.response.meta.dataType = "object"
        self.response.data = {
            "message": "Order partially updated"
        }

    def do_delete(self):
        self.response.status = RestStatus.status204
        self.response.data = None

    # optional
    def index(self):
        print("Content-Type: text/html; charset=utf-8")
        print()
        print("<h1>Order Controller</h1>")
