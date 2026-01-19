import json  # модуль для JSON серіалізації


class RestStatus:  # клас для статусу REST-відповіді
    def __init__(self, is_ok: bool, code: int, message: str):  # ініціалізація статусу
        self.is_ok = is_ok  # логічний прапорець успіху
        self.code = code  # код статусу
        self.message = message  # повідомлення статусу

    def to_json(self) -> dict:  # метод перетворює об'єкт у dict
        return {  # повертаємо словник
            "isOk": self.is_ok,  # прапорець успіху
            "code": self.code,  # код статусу
            "message": self.message,  # текст повідомлення
        }  # кінець dict


def main() -> None:  # головна функція демонстрації
    status = RestStatus(True, 200, "OK")  # створюємо приклад статусу
    result = json.dumps(status.to_json(), ensure_ascii=False)  # серіалізуємо у JSON
    print("RestStatus JSON:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
