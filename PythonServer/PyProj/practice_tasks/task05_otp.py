import random  # модуль для генерації випадкових чисел
import string  # модуль для наборів символів


class Helper:  # клас для генерації одноразового пароля (OTP)
    def generate_otp(self, length: int = 6) -> str:  # метод створює цифровий код заданої довжини
        digits = string.digits  # визначаємо набір цифр 0-9
        otp = "".join(random.choice(digits) for _ in range(length))  # формуємо OTP з випадкових цифр
        return otp  # повертаємо код у вигляді рядка


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    result = helper.generate_otp(6)  # генеруємо OTP довжиною 6
    print("OTP:", result)  # виводимо результат


if __name__ == "__main__":  # перевіряємо, що файл запускають напряму
    main()  # запускаємо приклад
