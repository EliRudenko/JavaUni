# datetime потрібен для iat (issue time) у JWT.
import datetime
# base64/binascii/re потрібні для перевірки Basic Auth.
import base64, binascii, re
# helper містить JWT-логіку (compose/validate).
from dao import helper
# Базовий REST-контролер + метадані.
from controllers.controller_rest import RestController, RestMeta, RestStatus, RestCache
# DAL для доступу до бази даних (перевірка логіна).
from dao.data_accessor import DataAccessor

# ДЗ 8, 9 — Тесты ошибок аутентификации + вывод заголовка JWT
# Реалізований контролер /user: Basic Auth -> JWT.

class UserController(RestController):

    def serve(self):
        # Базова настройка метаданих REST-відповіді.
        # Вказуємо лінки на методи API.
        self.response.meta = RestMeta(
           service="User API",
           links={
               "get": "GET /user",
               "post": "POST /user",
           }
        )
        super().serve()

    def do_get(self):
        # Аутентифікація користувача по Basic-схемі.
        # Тут багато перевірок, щоб показати різні сценарії помилок.
        self.response.meta.service += ": authentication"

        auth_header = self.request.headers.get("Authorization", None)
        if not auth_header:
            # 401 Unauthorized: немає заголовка Authorization.
            self.send_error("Unauthorized: Missing 'Authorization' header", 401)
            return
        
        # Саме Basic на початку заголовка.
        # Якщо схема інша (Bearer тощо) — повертаємо 401.
        auth_scheme = 'Basic '
        if not auth_header.startswith(auth_scheme):
            self.send_error(f"Unauthorized: Invalid 'Authorization' header format: {auth_scheme} only", 401)
            return
        
        credentials = auth_header[len(auth_scheme):]  # Все після "Basic ".
        if len(credentials) < 7:
            self.send_error("Unauthorized: Short 'Authorization' credentials value", 401)
            return
        
        # Валідація символів Base64: тільки стандартні символи + '=' в кінці.
        match = re.search(r"[^a-zA-Z0-9+/=]", credentials)
        if match:
            self.send_error(f"Unauthorized: Format error (invalid symbol) for credentials {credentials}", 401)
            return
        
        # Base64 -> строка "login:password" (стандартна форма Basic Auth).
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

        # Перевірка формату login:password.
        if not ':' in user_pass:
            self.send_error("Unauthorized: Credential format error", 401)
            return
        
        login, password = user_pass.split(':', 1)
        # Перевіряємо користувача через DAL (Data Access Layer).
        data_accessor = DataAccessor()
        user = data_accessor.authenticate(login, password)
        if not user:
            # 401: дані не підходять.
            self.send_error("Unauthorized: Credentials rejected", 401)
            return

        # Формуємо JWT payload.
        # iss повинен бути "Server-KN-P-221" за умовою завдання.
        payload = {
            "sub": str(user['user_id']),
            "iss": "Server-KN-P-221",
            "aud": user['role_id'],
            "iat": datetime.datetime.now().timestamp(),
            "name": user['user_name'],
            "email": user['user_email'],
        }
        # Налаштовуємо кеш: токен можна кешувати 1 годину.
        self.response.meta.cache = RestCache.hrs1
        self.response.meta.data_type = "token"
        self.response.data = helper.compose_jwt(payload)


    def do_post(self):
        # POST може використовуватися для реєстрації або створення ресурсів.
        # Тут демонстраційна відповідь із різними типами даних.
        self.response.meta.service += ": registration"
        self.response.meta.data_type = "object"
        self.response.data = {
            "int": 10,
            "float": 1e-3,
            "str": "POST",
            "cyr": "Привіт",
            "headers": self.request.headers,
        }

    
