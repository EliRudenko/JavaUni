import base64  # модуль для base64 кодування
import json  # модуль для серіалізації JSON


class Helper:  # клас для формування JWT body
    def create_jwt_body(self, header: dict, payload: dict) -> str:  # метод формує header.payload
        header_json = json.dumps(header, separators=(",", ":"))  # серіалізуємо header без пробілів
        payload_json = json.dumps(payload, separators=(",", ":"))  # серіалізуємо payload без пробілів
        header_bytes = header_json.encode("utf-8")  # переводимо header у bytes
        payload_bytes = payload_json.encode("utf-8")  # переводимо payload у bytes
        header_b64 = base64.urlsafe_b64encode(header_bytes).decode("utf-8").rstrip("=")  # base64url без =
        payload_b64 = base64.urlsafe_b64encode(payload_bytes).decode("utf-8").rstrip("=")  # base64url без =
        body = f"{header_b64}.{payload_b64}"  # поєднуємо дві частини через крапку
        return body  # повертаємо готове тіло JWT


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    header = {"alg": "HS256", "typ": "JWT"}  # приклад заголовка
    payload = {"sub": "123", "name": "Test User"}  # приклад навантаження
    result = helper.create_jwt_body(header, payload)  # формуємо тіло JWT
    print("JWT body:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
