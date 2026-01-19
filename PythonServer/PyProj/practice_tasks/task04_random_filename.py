import random  # модуль для генерації випадкових значень
import string  # модуль із наборами символів


class Helper:  # клас для генерації випадкових імен файлів
    def generate_filename(self, extension: str, length: int = 12) -> str:  # метод генерує ім'я з розширенням
        clean_extension = extension.lstrip(".")  # прибираємо крапку, якщо її передали
        symbols = string.ascii_lowercase + string.digits  # визначаємо дозволені символи
        name = "".join(random.choice(symbols) for _ in range(length))  # генеруємо випадкове ім'я
        filename = f"{name}.{clean_extension}"  # додаємо розширення до імені
        return filename  # повертаємо повну назву файлу


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    result = helper.generate_filename("txt")  # генеруємо ім'я файлу з розширенням txt
    print("Random filename:", result)  # виводимо результат


if __name__ == "__main__":  # перевіряємо, що запуск прямий
    main()  # викликаємо демонстрацію
