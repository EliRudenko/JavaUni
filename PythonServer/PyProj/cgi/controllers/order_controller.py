####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 25: CGI: стандартні потоки вводу/виводу та кодування
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 32: CGI: динамічний імпорт і типи контролерів (MVC, API)
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 33: CGI: JSON-серіалізація користувацьких об’єктів та non-dict типів
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 37: CGI: маршрутизація за HTTP-методами, семантика, статус 405
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 38: CGI: статус-коди 401 та 403
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 39: CGI: архітектура REST, огляд і реалізація в Python
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 40: CGI: ідемпотентність та кешування відповідей
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 41: CGI: тестування API-бекенду — способи, переваги, недоліки
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 25: НАСТРОЙКА ВЫВОДА В STDOUT И ФОРМИРОВАНИЕ HTTP-ОТВЕТА В CGI.
# - ВОПРОС 32: КОНТРОЛЛЕР ИСПОЛЬЗУЕТСЯ КАК ДИНАМИЧЕСКОЙ ПОДКЛЮЧАЕМЫЙ КЛАСС.
# - ВОПРОС 33: JSON-ОТВЕТЫ ФОРМИРУЮТСЯ ДЛЯ ПОЛЬЗОВАТЕЛЬСКИХ ДАННЫХ И СТАТУСОВ.
# - ВОПРОС 37: DO_GET/DO_POST/DO_PUT/DO_DELETE ДЕМОНСТРИРУЮТ РАЗНЫЕ HTTP-МЕТОДЫ И 405.
# - ВОПРОС 38: СТАТУСЫ 401/403 ВЫДАЮТСЯ ПРИ НАРУШЕНИИ ДОСТУПА.
# - ВОПРОС 39: REST-МЕТОДЫ ОПИСЫВАЮТ ДЕЙСТВИЯ С РЕСУРСОМ ORDER.
# - ВОПРОС 40: КЭШ-ОБЪЕКТЫ И КОММЕНТАРИИ ПРО ИДЕМПОТЕНТНОСТЬ В МЕТОДАХ ЗАКАЗОВ.
# - ВОПРОС 41: МЕТОДЫ КОНТРОЛЛЕРА УДОБНЫ ДЛЯ ТЕСТОВ API (GET/POST/PUT/DELETE).
#


# datetime потрібен для server_time у метаданих.
import datetime
# json потрібен для серіалізації REST-відповідей.
import json
# sys використовується для виводу CGI-відповіді в stdout.
import sys

# CgiRequest містить дані CGI-запиту (метод, заголовки, маршрут).
from models.request import CgiRequest

# ДЗ 5, 6, 7 — API /order, Custom-Header, REST-ответы
# реализован REST-контроллер для заказов

# REST-статус (логічний, у JSON-відповіді).
class RestStatus:
    def __init__(self,is_ok:bool, code:int, message:str):
        self.is_ok = is_ok
        self.code = code
        self.message = message

    def to_json(self):
        # Перетворення статусу у словник для JSON.
        return {
            "isOk": self.is_ok,
            "code": self.code,
            "message": self.message
        }

# Попередньо визначені REST-статуси.
RestStatus.status200 = RestStatus(True, 200, "OK")
RestStatus.status405 = RestStatus(False, 405, "Method Not Allowed")
RestStatus.status201 = RestStatus(True, 201, "Created")
RestStatus.status204 = RestStatus(True, 204, "No Content")
RestStatus.status403 = RestStatus(False, 403, "Forbidden")
RestStatus.status404 = RestStatus(False, 404, "Not Found")


# Дані про кешування відповіді.
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

# Варіанти кешу: без кешування, кеш 1 година.
RestCache.no = RestCache()
RestCache.hrs1 = RestCache(lifetime=60*60)

# Метадані REST-відповіді (сервіс, метод, кеш, час сервера).
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

# Структура REST-відповіді: status + meta + data.
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
        # В контроллере хранится объект запроса и объект ответа
        self.request = request
        self.response = None


    def _check_custom_header(self):
        # Проверка обязательного заголовка.
        # Якщо його нема — повертаємо 403 Forbidden (доступ заборонено).
        # Це приклад авторизації по кастомному заголовку.
        header = self.request.headers.get("Custom-Header")
        
        if header is None or header.strip() == "":
            self.response.status = RestStatus.status403
            self.response.status.message = "Forbidden: Missing required header 'Custom-Header'"
            self.response.meta.data_type = "null"
            self.response.data = None
            return False
        return True

    def serve(self):
        # Ініціалізація базової структури REST-відповіді.
        # Відповідь завжди містить meta + status + data.
        self.response = RestResponse(meta=RestMeta(
            service="Order API",
            request_method=self.request.request_method,
            links={
                "get": "GET /order/{id}",
                "post": "POST /order",
                "put": "PUT /order/{id}",
                "patch": "PATCH /order/{id}",
                "delete": "DELETE /order/{id}",
            }
        ))

        # Спочатку перевіряємо наявність Custom-Header.
        # Для навчання частина запитів без нього -> 403.
        if not self._check_custom_header():
            self._send_response()
            return

        # Далі вибираємо метод обробки за HTTP-методом запиту.
        # Якщо методу немає — 405 Method Not Allowed.
        action = "do_" + self.request.request_method.lower()
        controller_action = getattr(self, action, None)
        
        if controller_action:
            controller_action()
        else:
            self.response.status = RestStatus.status405
        self._send_response()

    def _send_response(self):
        # Окрема функція для виводу HTTP-відповіді.
        # Тут формуємо HTTP status line і Content-Type.
        http_status_line = f"Status: {self.response.status.code} {self.response.status.message}\n"
        sys.stdout.buffer.write(http_status_line.encode('utf-8'))
        sys.stdout.buffer.write(b"Content-Type: application/json; charset=utf-8\n\n")

        # JSON-серіалізація: default=... дозволяє серіалізувати non-dict об'єкти.
        json_output = json.dumps(
            self.response,
            ensure_ascii=False,
            default=lambda x: x.to_json() if hasattr(x, 'to_json') else str,
        )

        sys.stdout.buffer.write(json_output.encode('utf-8'))


    def do_get(self):
        # GET є ідемпотентним: повторний виклик не змінює стан.
        # Тут повертаємо приклад об'єкта замовлення.
        self.response.meta.data_type = "object"
        self.response.meta.cache = RestCache.hrs1 
        self.response.data = {
            "order_id": 12345,
            "status": "shipped",
            "total_price": 499.99,
            "method": "GET"
        }

    def do_post(self):
        # POST не ідемпотентний: створює новий ресурс.
        self.response.meta.data_type = "object"
        self.response.status = RestStatus.status201 
        self.response.data = {
            "order_id": 12346,
            "message": "Order successfully created",
            "method": "POST",
            "payload_received": getattr(self.request, 'data', {}) 
        }

    def do_put(self):
        # PUT ідемпотентний: заміна ресурсу дає однаковий результат при повторенні.
        self.response.meta.data_type = "object"
        self.response.data = {
            "order_id": 12345,
            "message": "Order successfully replaced (PUT)",
            "method": "PUT"
        }

    def do_patch(self):
        # PATCH частково змінює ресурс (може бути неіде-потентний).
        self.response.meta.data_type = "object"
        self.response.data = {
            "order_id": 12345,
            "message": "Order successfully updated (PATCH)",
            "method": "PATCH"
        }

    def do_delete(self):
        # DELETE ідемпотентний: повторне видалення не змінює результат.
        self.response.status = RestStatus.status204 
        self.response.data = None 
        self.response.meta.data_type = "null"
