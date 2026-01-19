import random  # модуль для простого генератора випадкових чисел
import string  # модуль для наборів символів


def generate_salt(length: int = 16) -> str:  # функція генерує випадкову «сіль» заданої довжини
    symbols = string.ascii_letters + string.digits  # формуємо набір літер і цифр для солі
    return "".join(random.choice(symbols) for _ in range(length))  # збираємо строку з випадкових символів
