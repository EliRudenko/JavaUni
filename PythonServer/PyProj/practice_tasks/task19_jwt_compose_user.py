import base64  # модуль для base64 кодування
import hashlib  # модуль для хеш-функцій
import hmac  # модуль для HMAC-підпису
import json  # модуль для JSON


class Helper:  # клас для формування JWT з user-даних
    def create_jwt_for_user(self, user: dict, secret: str = "secret") -> str:  # метод створює JWT з user{id,name,email}
        header = {"alg": "HS256", "typ": "JWT"}  # стандартний заголовок JWT
        payload = {  # формуємо payload з user-даних
            "id": user.get("id"),  # ідентифікатор користувача
            "name": user.get("name"),  # ім'я користувача
            "email": user.get("email"),  # email користувача
        }  # завершуємо payload
        header_json = json.dumps(header)  # серіалізуємо header
        payload_json = json.dumps(payload)  # серіалізуємо payload
        header_b64 = base64.urlsafe_b64encode(header_json.encode("utf-8"))  # кодуємо header у base64url
        payload_b64 = base64.urlsafe_b64encode(payload_json.encode("utf-8"))  # кодуємо payload у base64url
        body = header_b64 + b"." + payload_b64  # формуємо тіло JWT
        mac = hmac.new(secret.encode("utf-8"), body, hashlib.sha256)  # створюємо HMAC-SHA256
        signature = base64.urlsafe_b64encode(mac.digest())  # кодуємо підпис у base64url
        token = body + b"." + signature  # збираємо повний JWT
        return token.decode("utf-8")  # повертаємо токен


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    user = {"id": 1, "name": "Alice", "email": "alice@example.com"}  # приклад user-даних
    result = helper.create_jwt_for_user(user)  # створюємо JWT
    print("JWT token:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
