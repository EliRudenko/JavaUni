####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 25: CGI: стандартні потоки вводу/виводу та кодування
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 33: CGI: JSON-серіалізація користувацьких об’єктів та non-dict типів
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 37: CGI: маршрутизація за HTTP-методами, семантика, статус 405
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 39: CGI: архітектура REST, огляд і реалізація в Python
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 40: CGI: ідемпотентність та кешування відповідей
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 25: ФОРМИРОВАНИЕ ТЕЛА ОТВЕТА В STDOUT И НАСТРОЙКИ КОДИРОВКИ.
# - ВОПРОС 33: СЕРИАЛИЗАЦИЯ В JSON ДЛЯ REST-ОТВЕТОВ ПОКАЗЫВАЕТ ПРИВЕДЕНИЕ NON-DICT ДАННЫХ.
# - ВОПРОС 37: ПРОВЕРКА HTTP-МЕТОДОВ И ОТВЕТ 405 ПОКАЗЫВАЮТ МАРШРУТИЗАЦИЮ ПО МЕТОДАМ.
# - ВОПРОС 39: REST-ОТВЕТЫ ФОРМИРУЮТСЯ В ЕДИНОМ ФОРМАТЕ.
# - ВОПРОС 40: RestCache И МЕТАДАННЫЕ КЕША ДЕМОНСТРИРУЮТ ИДЕМПОТЕНТНОСТЬ/КЕШИРОВАНИЕ.
#


# datetime використовується для server_time у метаданих відповіді.
import datetime
# json потрібен для серіалізації REST-відповідей.
import json
# sys потрібен для запису відповіді у stdout (CGI).
import sys

# CgiRequest містить дані запиту, сформовані диспетчером.
from models.request import CgiRequest


# REST-статус: "логічний" статус у JSON (не плутати з HTTP-статусом).
class RestStatus:
    def __init__(self,is_ok:bool, code:int, message:str):
        self.is_ok = is_ok
        self.code = code
        self.message = message

    def to_json(self):
        # Повертаємо словник для подальшої JSON-серіалізації.
        return {
            "isOk": self.is_ok,
            "code": self.code,
            "message": self.message
        }

# Попередньо визначені статуси (приклади відповідей API).
RestStatus.status200 = RestStatus(True, 200, "OK")
RestStatus.status401 = RestStatus(False, 401, "Unauthorized")
RestStatus.status405 = RestStatus(False, 405, "Method Not Allowed")

# Інформація про кешування (для клієнта).
class RestCache:
    def __init__(self, exp:str|int|None=None, lifetime:int|None=None):
        self.exp = exp
        self.lifetime = lifetime

    def to_json(self):
        # Відображаємо кеш-поля у формат, зручний для JSON.
        return {
            "exp": self.exp,
            "lifetime": self.lifetime,
            "units": "seconds"
        }

# Варіанти кешування: без кешу та 1 година.
RestCache.no = RestCache()
RestCache.hrs1 = RestCache(lifetime=60*60)

# Метадані відповіді: сервіс, метод, час, кеш, лінки тощо.
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
        # Метадані перетворюємо у словник для JSON.
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

# Загальна структура REST-відповіді: status + meta + data.
class RestResponse:
    def __init__(self, meta:RestMeta|None=None,
                  status:RestStatus=RestStatus.status200, data:any=None):
        self.status = status
        self.meta = meta
        self.data=data

    def to_json(self):
        # Перетворення в JSON-ready структуру.
        return {
            "status": self.status.to_json(),
            "meta": self.meta.to_json(),
            "data": self.data,
        }
    



# Базовий REST-контролер, що маршрутизує за HTTP-методом.
class RestController:

    def __init__(self, request:CgiRequest):
        # Зберігаємо об'єкт запиту і готуємо структуру відповіді.
        self.request = request
        self.response = RestResponse()
        
    def serve(self):
        # Базова ініціалізація метаданих.
        if self.response.meta is None:
            self.response.meta = RestMeta(
                service="Rest default service",
            )
        # Записуємо HTTP-метод у метадані (GET/POST/PUT...).
        self.response.meta.request_method = self.request.request_method

        # Маршрутизація за HTTP-методом: do_get, do_post, do_put...
        action = "do_" + self.request.request_method.lower()
        controller_action = getattr(self, action, None)
        if controller_action:
            controller_action()
        else:
            # Якщо метод не підтримується — 405 Method Not Allowed.
            self.response.status = RestStatus.status405
        # Виводимо JSON через stdout (CGI).
        sys.stdout.buffer.write(b"Content-Type: application/json; charset=utf-8\n\n")
        # default=... дозволяє серіалізувати non-dict об'єкти (через to_json або str).
        sys.stdout.buffer.write(json.dumps(
            self.response,
            ensure_ascii=False,
            default=lambda x: x.to_json() if hasattr(x, 'to_json') else str,
        ).encode())
        
    def send_error(self, message: str, status_code: int):
        # Уніфікований спосіб повернути помилку в JSON.
        self.response.status = RestStatus(False, status_code, "Error")
        self.response.meta.cache = RestCache.no
        self.response.meta.data_type = "string"
        self.response.data = message
        
    def send_header_missing_response(self, header_name:str):
        # Приклад авторизаційної помилки: 403 Forbidden.
        self.response.status = RestStatus(False, 403, f"Forbidden: Missing required header '{header_name}'")
        self.response.meta.data_type = "null"
        self.response.data = None
