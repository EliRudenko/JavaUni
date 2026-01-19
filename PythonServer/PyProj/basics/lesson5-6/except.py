import logging

logging.basicConfig(level=logging.INFO, format="%(levelname)s: %(message)s")

class AppError(Exception): pass

def risky_action() -> None:
    logging.info("Выполняется risky_action()")
    raise TypeError("Некорректный тип данных")

def main() -> None:
    try:
        risky_action()
    except TypeError as e:
        logging.error(f"Ошибка типа: {e}")
    except Exception as e:
        logging.exception(f"Неизвестная ошибка: {e}")
    else:
        logging.info("Ошибок нет")
    finally:
        logging.info("Завершение")

if __name__ == "__main__":
    main()
