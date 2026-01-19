import json  # модуль для роботи з форматом JSON


def main() -> None:  # головна функція для запуску прикладу
    try:  # блок перевірки, щоб відловити помилки файлу або JSON
        file = open("../db.json", "r", encoding="utf-8")  # відкриваємо файл конфігурації бази даних
        data = json.load(file)  # читаємо JSON і перетворюємо його у Python-об'єкт
        file.close()  # закриваємо файл після читання
        if not isinstance(data, dict):  # перевіряємо, що корінь JSON — це dict
            raise ValueError("JSON root must be an object")  # піднімаємо помилку, якщо не dict
        if "dbName" not in data:  # перевіряємо наявність обов'язкового поля
            raise KeyError("dbName")  # піднімаємо помилку, якщо поля немає
    except FileNotFoundError as err:  # обробляємо ситуацію, коли файл не знайдено
        print("DB config file not found:", err)  # виводимо повідомлення про відсутній файл
    except json.decoder.JSONDecodeError as err:  # обробляємо некоректний JSON
        print("Invalid JSON:", err)  # виводимо повідомлення про помилку JSON
    except (ValueError, KeyError) as err:  # обробляємо логічні помилки перевірок
        print("Invalid DB config:", err)  # виводимо повідомлення про неправильну конфігурацію
    else:  # якщо помилок не було
        print("DB config is valid:", data)  # показуємо, що конфігурація валідна


if __name__ == "__main__":  # перевіряємо, що файл запускають напряму
    main()  # запускаємо демонстрацію
