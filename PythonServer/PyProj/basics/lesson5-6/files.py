import logging
from pathlib import Path

logging.basicConfig(level=logging.INFO, format="%(levelname)s: %(message)s")

CONFIG_PATH = Path("db.ini")


def create_file(path: Path = CONFIG_PATH) -> None:
    path.write_text(
        "# Данные для подключения к БД\nhost=localhost\nport=3306\n", encoding="utf-8"
    )
    logging.info(f"Файл успешно создан: {path.resolve()}")


def parse_ini(filename: str) -> dict | None:

    try:
        with open(filename, encoding="utf-8") as file:
            return dict(
                map(
                    lambda line: tuple(part.strip() for part in line.split(":", 1)),
                    filter(lambda l: ":" in l and not l.strip().startswith("#"), file)
                )
            )
    except OSError as osError:
        print(f"OS error: {osError}")
        return None



def main() -> None:
    create_file()
    config_dict = parse_ini()
    logging.info(f"Распарсенный конфиг (map+filter): {config_dict}")


if __name__ == "__main__":
    main()
