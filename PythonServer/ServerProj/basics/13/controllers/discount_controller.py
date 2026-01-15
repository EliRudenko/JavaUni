from controllers.controller_rest import RestController, RestMeta, RestStatus, RestCache


class DiscountController(RestController):

    def serve(self) :
        self.response.meta = RestMeta(
                service="Discount API",
                links={
                    "get": "GET /discount",
                    "post": "POST /discount",
                }
            )
        
        super().serve()
        

    def do_get(self):
        self.response.meta.service += ": Users`s bonuses"
        self.response.meta.cache = RestCache.hrs1
        self.response.meta.dataType = "object"
        self.response.data = {
            "int": 10,
            "float": 1e-3,
            "str": "GET",
            "cyr": "Вітання",
            "headers": self.request.headers
        }
