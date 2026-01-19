# Модуль os використовується для роботи з файлами/директоріями.
import os

# Створення та запис у файл із перевірками існування.
def create_file() -> None :
    filename = 'db.ini'  # Ім'я файлу для створення.
    file = None          # Змінна для file handle.
    try :
        # Перевіряємо, чи файл існує і не порожній.
        if os.path.exists(filename) and os.path.getsize(filename) > 0:
            print(f"File '{filename}' already exists and is not empty. Skipping creation.")
            return
        # Відкриваємо файл у режимі запису (w).
        file = open(filename, mode="w", encoding='utf-8')
        file.write("Дані для підключення БД\n")  # Записуємо рядок.
        file.write('host: localhost\n')          # Записуємо рядок.
        file.write('port: 3306')                 # Записуємо рядок.
        file.flush()                             # Примусовий запис у файл.
    except OSError as err :
        # Обробка помилок файлової системи.
        print("Error writing file", err)
    else :
        # Виконується, якщо помилок не було.
        print("File write ok")
    finally :
        # Завжди закриваємо файл, якщо він був відкритий.
        if file != None :
            file.close()


# Вивід вмісту файлу у консоль.
def print_file(filename:str) -> None :
    file = None
    try :
        file = open(filename, encoding='utf-8')  # Відкриття файлу у режимі читання.
        print(file.read())                       # Читаємо весь файл.
    except OSError as err :
        print("Error read file", err)
    else :
        print("----------EDF--------")
    finally :
        if file != None :
            file.close()

            
# Безпечне читання файлу через контекстний менеджер.
def read_as_string(filename:str)->str :
    try :
        with open(filename, encoding='utf-8') as file :
            return file.read()  # Повертаємо текст файлу.
    except OSError as err :
        print("Error read file", err)
        return None  

   
# Примітивний парсер INI-даних: розбирає "ключ: значення".
def parse_ini_imp(filename:str) -> dict|None :
    ret={}  # Словник для накопичення результатів.
    try :
        with open(filename, encoding='utf-8') as file :
            for line in file :
                if ':' in line :
                    k, v = line.split(':', 1)  # Розділяємо на ключ/значення.
                    ret[k.strip()] =v.strip()  # Очищаємо пробіли.
                
        return ret
    except OSError as err :
        print("Error read file", err)
        return None  


# Розширений парсер INI: ігнорує коментарі і порожні рядки.
def parse_ini(filename: str) -> dict | None:
    try:
        with open(filename, encoding='utf-8') as file:
            result = {}  # Словник для параметрів.
            for line in file:
                line = line.strip()  # Прибираємо пробіли по краях.
                if not line or line.startswith('#') or line.startswith(';'):
                    continue  # Пропускаємо порожні рядки і коментарі.
                if ':' in line:
                    key, value = map(str.strip, line.split(':', 1))
                    key, value = map(str.strip, line.split(':', 1))
                    for comment_char in ['#', ';']:
                        value = value.split(comment_char, 1)[0].strip()
                    result[key] = value  # Записуємо у словник.
            return result
    except OSError as err:
        print("Error read file", err)
        return None


# Демонстрація файлових операцій.
def main() -> None :
    create_file()
    print_file("db.ini")
    print(read_as_string("db.ini"))
    print(parse_ini_imp("db.ini"))
    print(parse_ini("db.ini"))


if __name__ == '__main__' :
    main()
