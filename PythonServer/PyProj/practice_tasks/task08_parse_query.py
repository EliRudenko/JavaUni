from urllib.parse import parse_qs  # модуль для розбору query-рядка


class Helper:  # клас для розбору query-параметрів
    def parse_query(self, query: str) -> dict:  # метод повертає dict ключ-значення
        normalized = query.lstrip("?")  # прибираємо знак ? на початку
        parsed = parse_qs(normalized, keep_blank_values=True)  # отримуємо dict зі списками значень
        result = {}  # створюємо порожній словник для результату
        for key in parsed:  # перебираємо всі ключі
            values = parsed[key]  # беремо список значень для ключа
            result[key] = values[0] if len(values) == 1 else values  # спрощуємо до одного значення або списку
        return result  # повертаємо готовий dict


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    result = helper.parse_query("?paid=true&tag=vip&tag=fast")  # розбираємо приклад query
    print("Parsed query:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # стартуємо демонстрацію
