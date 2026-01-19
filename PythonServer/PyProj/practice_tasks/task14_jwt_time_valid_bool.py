import base64  # модуль для base64 декодування
import json  # модуль для JSON
import time  # модуль для роботи з часом


class Helper:  # клас для перевірки часу дії JWT
    def is_time_valid(self, token: str) -> bool:  # метод повертає True/False
        parts = token.split(".")  # ділимо JWT на частини
        if len(parts) != 3:  # перевіряємо коректність формату
            raise ValueError("Invalid JWT format")  # піднімаємо помилку для некоректного токена
        payload_json = base64.urlsafe_b64decode(parts[1] + "==").decode("utf-8")  # декодуємо payload
        payload = json.loads(payload_json)  # перетворюємо payload у dict
        now = time.time()  # беремо поточний час у секундах
        if "nbf" in payload and payload["nbf"] > now:  # перевіряємо, що токен вже активний
            return False  # токен ще не дійсний
        if "exp" in payload and payload["exp"] < now:  # перевіряємо, чи токен не прострочений
            return False  # токен вже прострочений
        return True  # якщо перевірки пройдено, токен валідний


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    token = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.eyJleHAiOiAyMDAwMDAwMDAwLCJuYmYiOiAxMDAwMDAwMDAwfQ.signature"  # приклад токена з часом
    result = helper.is_time_valid(token)  # перевіряємо час дії
    print("Time valid:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
