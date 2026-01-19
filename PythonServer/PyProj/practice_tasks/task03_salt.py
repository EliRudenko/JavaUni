import random  # модуль для генерації випадкових чисел
import string  # модуль з наборами символів


class Helper:  # клас для генерації криптографічної солі
    def generate_salt(self, length: int = 16) -> str:  # метод повертає випадкову строку потрібної довжини
        # THIS METHOD WAS PARTIALLY PRESENT AND IS NOW FULLY COMPLETED.  # вимога позначити частково готове місце
        symbols = string.ascii_letters + string.digits  # формуємо набір літер і цифр
        salt = "".join(random.choice(symbols) for _ in range(length))  # будуємо випадкову строку
        return salt  # повертаємо готову сіль


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    result = helper.generate_salt(16)  # генеруємо сіль довжиною 16
    print("Salt:", result)  # показуємо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
