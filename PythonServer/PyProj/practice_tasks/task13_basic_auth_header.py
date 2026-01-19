import base64  # модуль для base64 кодування/декодування


class Helper:  # клас для роботи з Authorization Basic
    def parse_basic(self, header: str) -> dict:  # метод повертає userId та userPass
        if not header.startswith("Basic "):  # перевіряємо, що схема Basic
            raise ValueError("Invalid Authorization scheme")  # помилка, якщо схема не Basic
        encoded = header[len("Basic "):]  # беремо частину після 'Basic '
        decoded_bytes = base64.b64decode(encoded)  # декодуємо base64 у bytes
        decoded_text = decoded_bytes.decode("utf-8")  # перетворюємо bytes у текст
        if ":" not in decoded_text:  # перевіряємо наявність розділювача
            raise ValueError("Invalid Basic credentials")  # помилка, якщо немає двокрапки
        user_id, user_pass = decoded_text.split(":", 1)  # ділимо на логін і пароль
        result = {  # формуємо dict результату
            "userId": user_id,  # кладемо ідентифікатор користувача
            "userPass": user_pass,  # кладемо пароль користувача
        }  # завершуємо dict
        return result  # повертаємо результат


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    header = "Basic dXNlcjE6cGFzczEyMw=="  # приклад Basic заголовка (user1:pass123)
    result = helper.parse_basic(header)  # розбираємо заголовок
    print("Basic data:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
