import base64  # модуль для base64 кодування/декодування
import json  # модуль для роботи з JSON


class Helper:  # клас з методом для декомпозиції JWT
    def split_jwt(self, token: str) -> dict:  # метод розбиває JWT на header/payload/signature
        parts = token.split(".")  # ділимо токен по крапці
        if len(parts) != 3:  # перевіряємо, що є три частини
            raise ValueError("Invalid JWT format")  # піднімаємо помилку для некоректного формату
        header_json = base64.urlsafe_b64decode(parts[0] + "==").decode("utf-8")  # декодуємо header з base64url
        payload_json = base64.urlsafe_b64decode(parts[1] + "==").decode("utf-8")  # декодуємо payload з base64url
        header = json.loads(header_json)  # перетворюємо header у dict
        payload = json.loads(payload_json)  # перетворюємо payload у dict
        result = {  # формуємо результат
            "header": header,  # кладемо header
            "payload": payload,  # кладемо payload
            "signature": parts[2],  # кладемо signature як строку
        }  # завершуємо dict
        return result  # повертаємо результат


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    sample_token = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9.eyJzdWIiOiAiMTIzIiwgIm5hbWUiOiAiVGVzdCJ9.signature"  # приклад JWT
    result = helper.split_jwt(sample_token)  # викликаємо метод декомпозиції
    print("JWT parts:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
