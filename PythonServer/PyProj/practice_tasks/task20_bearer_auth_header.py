class Helper:  # клас для роботи з Authorization Bearer
    def parse_bearer(self, header: str) -> str:  # метод повертає токен або кидає помилку
        if not header.startswith("Bearer "):  # перевіряємо наявність схеми Bearer
            raise ValueError("Invalid Authorization scheme")  # помилка, якщо схема не Bearer
        token = header[len("Bearer "):]  # беремо токен після 'Bearer '
        if not token:  # перевіряємо, що токен не порожній
            raise ValueError("Empty Bearer token")  # помилка, якщо токен порожній
        return token  # повертаємо токен


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    header = "Bearer abc.def.ghi"  # приклад Bearer заголовка
    result = helper.parse_bearer(header)  # розбираємо заголовок
    print("Bearer token:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
