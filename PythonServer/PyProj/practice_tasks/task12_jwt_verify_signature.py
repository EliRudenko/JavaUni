import base64  # модуль для base64 кодування
import hashlib  # модуль для хеш-функцій
import hmac  # модуль для HMAC-підпису


class Helper:  # клас для перевірки підпису JWT
    def verify_signature(self, token: str, secret: str) -> bool:  # метод перевіряє, чи валідний підпис
        parts = token.split(".")  # ділимо токен на частини
        if len(parts) != 3:  # перевіряємо формат токена
            raise ValueError("Invalid JWT format")  # помилка, якщо формат неправильний
        body = parts[0] + "." + parts[1]  # формуємо тіло JWT для підпису
        body_bytes = body.encode("utf-8")  # кодуємо тіло у bytes
        secret_bytes = secret.encode("utf-8")  # кодуємо секрет у bytes
        mac = hmac.new(secret_bytes, body_bytes, hashlib.sha256)  # створюємо HMAC-SHA256
        signature = base64.urlsafe_b64encode(mac.digest()).decode("utf-8").rstrip("=")  # отримуємо base64url підпис
        return signature == parts[2]  # порівнюємо підпис з тим, що в JWT


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    token = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.eyJzdWIiOiAiMTIzIn0.M8bKuySI7y1z5uFf0sX_W3hcU8Mfa5tgb2CGqg6Pw_w"  # приклад токена
    result = helper.verify_signature(token, "secret")  # перевіряємо підпис токена
    print("Signature valid:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
