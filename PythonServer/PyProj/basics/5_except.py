####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 09: Виняткові ситуації та помилки. Оброблення винятків, блок try
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 09: БЛОК TRY/EXCEPT/ELSE/FINALLY ПОКАЗЫВАЕТ ОБРАБОТКУ ИСКЛЮЧЕНИЙ И ПОЧЕМУ ОНА НУЖНА.
#


# Функція навмисно кидає виняток TypeError.
def throws() -> None:
    print("Throws error")  # Повідомляємо, що зараз буде помилка.
    raise TypeError  # Кидаємо виняток без повідомлення.


# Функція кидає ValueError із повідомленням.
def throw_with_msg() -> None:
    raise ValueError("This is a ValueError")


# Функція, яка не кидає винятків (порожнє тіло).
def not_throws() -> None:
    pass


# Демонстрація роботи try/except/else/finally.
def main() -> None:
    try:
        throws()  # Викликаємо функцію, яка кидає виняток.
        return    # Цей рядок не виконається через помилку вище.
    except:
        # Перехоплення будь-якого винятку (небажано в продакшн-коді).
        print("Caught an error")
        
    try:
        throw_with_msg()  # Кидає ValueError з текстом.
    except TypeError:
        # Цей блок не виконається, бо виняток інший.
        print("Caught a TypeError")
    except ValueError as e:
        # Перехоплюємо ValueError та читаємо повідомлення.
        print(e)
    except Exception as e:
        # Узагальнений перехоплювач (на випадок інших помилок).
        print(f"Caught a generic exception: {e}")
    finally:
        # Виконується завжди: і при успіху, і при помилці.
        print("Finally block executed")

    try:
        not_throws()  # Тут помилок не буде.
    except:
        print("This will not be printed")
    else:
        # Виконується тільки якщо не було винятку.
        print("Continue")
    finally:
        # Завжди виконується.
        print("Finally")



if __name__ == "__main__":
    main()
