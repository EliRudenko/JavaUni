import hashlib  # модуль для криптографічних хеш-функцій


class Helper:  # клас, який містить допоміжний метод для хешування
    def hash_digest(self, message: str) -> str:  # метод рахує хеш-дайджест рядка
        data = message.encode("utf-8")  # перетворюємо текст у байти
        digest = hashlib.sha256(data).hexdigest()  # обчислюємо SHA-256 і переводимо у hex-рядок
        return digest  # повертаємо готовий дайджест


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр класу Helper
    result = helper.hash_digest("Hello, world!")  # викликаємо метод для тестового рядка
    print("Hash digest:", result)  # виводимо результат у консоль


if __name__ == "__main__":  # перевірка, що файл запускають напряму
    main()  # стартуємо демонстрацію
