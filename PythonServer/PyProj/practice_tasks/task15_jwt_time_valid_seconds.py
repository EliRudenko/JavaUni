import base64  # модуль для base64 декодування
import json  # модуль для JSON
import time  # модуль для роботи з часом


class Helper:  # клас для підрахунку часу дії JWT
    def seconds_to_expire(self, token: str):  # метод повертає секунди до завершення
        parts = token.split(".")  # ділимо JWT на частини
        if len(parts) != 3:  # перевіряємо формат токена
            raise ValueError("Invalid JWT format")  # помилка для некоректного токена
        payload_json = base64.urlsafe_b64decode(parts[1] + "==").decode("utf-8")  # декодуємо payload
        payload = json.loads(payload_json)  # перетворюємо payload у dict
        now = time.time()  # поточний час у секундах
        if "nbf" in payload and now < payload["nbf"]:  # перевіряємо, чи дія ще не почалась
            return now - payload["nbf"]  # повертаємо від'ємну різницю
        if "exp" not in payload:  # якщо exp немає
            return None  # повертаємо None
        if payload["exp"] < now:  # якщо строк дії вже завершено
            return None  # повертаємо None
        return payload["exp"] - now  # повертаємо позитивний час до завершення


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    token = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.eyJuYmYiOiAxMDAwMDAwMDAwLCJleHAiOiAyMDAwMDAwMDAwfQ.signature"  # приклад токена
    result = helper.seconds_to_expire(token)  # отримуємо секунди до завершення
    print("Seconds to expire:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
