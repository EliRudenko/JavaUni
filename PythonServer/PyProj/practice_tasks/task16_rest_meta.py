import datetime  # модуль для отримання часу
import json  # модуль для JSON серіалізації


class RestMeta:  # клас для метаданих REST-відповіді
    def __init__(self, service: str, request_method: str):  # ініціалізація метаданих
        self.service = service  # назва сервісу
        self.request_method = request_method  # HTTP-метод запиту
        self.server_time = datetime.datetime.now().timestamp()  # час відповіді сервера

    def to_json(self) -> dict:  # метод перетворює об'єкт у dict
        return {  # повертаємо словник
            "service": self.service,  # назва сервісу
            "request_method": self.request_method,  # HTTP-метод
            "server_time": self.server_time,  # час сервера
        }  # кінець dict


def main() -> None:  # головна функція демонстрації
    meta = RestMeta("OrderService", "GET")  # створюємо приклад метаданих
    result = json.dumps(meta.to_json(), ensure_ascii=False)  # серіалізуємо у JSON-строку
    print("RestMeta JSON:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
