class Helper:  # клас для розбору path-частини URL
    def parse_path(self, path: str) -> dict:  # метод повертає controller/action/id
        cleaned = path.strip("/")  # прибираємо зайві слеші з країв
        parts = cleaned.split("/") if cleaned else []  # ділимо шлях на частини або даємо порожній список
        controller = parts[0] if len(parts) > 0 else None  # перший елемент як controller
        action = parts[1] if len(parts) > 1 else None  # другий елемент як action
        identifier = parts[2] if len(parts) > 2 else None  # третій елемент як id
        result = {  # формуємо словник результатів
            "controller": controller,  # кладемо controller
            "action": action,  # кладемо action
            "id": identifier,  # кладемо id
        }  # завершуємо dict
        return result  # повертаємо результат


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    result = helper.parse_path("/orders/list/42")  # розбираємо приклад path
    print("Parsed path:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
