import base64  # модуль для base64 кодування
import hashlib  # модуль для хеш-функцій
import hmac  # модуль для HMAC-підпису


class Helper:  # клас для формування підпису JWT
    def create_jwt_signature(self, body: str, secret: str) -> str:  # метод підписує body за допомогою секрету
        body_bytes = body.encode("utf-8")  # кодуємо body у bytes
        secret_bytes = secret.encode("utf-8")  # кодуємо секрет у bytes
        mac = hmac.new(secret_bytes, body_bytes, hashlib.sha256)  # створюємо HMAC з SHA-256
        signature = base64.urlsafe_b64encode(mac.digest()).decode("utf-8").rstrip("=")  # кодуємо підпис у base64url
        return signature  # повертаємо підпис


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    body = "header.payload"  # приклад готового body JWT
    result = helper.create_jwt_signature(body, "super-secret")  # формуємо підпис
    print("JWT signature:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
