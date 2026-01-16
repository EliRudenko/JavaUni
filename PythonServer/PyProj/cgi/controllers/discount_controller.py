from controllers.controller_rest import RestController, RestMeta, RestCache
from dao import helper


class DiscountController(RestController):

    def serve(self):
        self.response.meta = RestMeta(
           service="Discount API",
           links={
               "get": "GET /discount",
               "program": "GET /discount/program",
               "post": "POST /discount",
           }
        )
        super().serve()

    def do_get(self):
        if len(self.request.path_parts) > 1:
            if self.request.path_parts[1] == "program":
                return self.get_program()
        self.get_default()

    def do_post(self):
        self.response.meta.service += ": post"
        self.response.meta.data_type = "object"
        self.response.data = {
            "int": 10,
            "float": 1e-3,
            "str": "POST",
            "cyr": "Привіт",
            "headers": self.request.headers,
        }

    def get_default(self):
        self.response.meta.service += ": User`s bonuses"

        try:
            payload = helper.jwt_payload_from_request(self.request, True)
        except ValueError as e:
            self.send_error(str(e), 401)
            return

        self.response.meta.data_type = "object"
        self.response.meta.cache = RestCache.hrs1
        self.response.data = payload

    def get_program(self):
        self.response.meta.service += ": Bonus program info"
        payload = helper.jwt_payload_from_request(self.request)

        self.response.meta.data_type = "object"
        self.response.meta.cache = RestCache.hrs1
        self.response.data = {
            "5%": "1000-10000",
            "10%": "10001-50000",
            "payload": payload
        }