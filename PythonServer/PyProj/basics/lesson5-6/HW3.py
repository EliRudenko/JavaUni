import logging
from pathlib import Path

logging.basicConfig(level=logging.INFO, format="%(levelname)s: %(message)s")

CONFIG_PATH = Path("db.ini")


def create_file(path: Path = CONFIG_PATH) -> None:
    path.write_text(
        "# Дані для підключення БД (взято из дз)\n"
        "host: localhost  # розміщення БД\n"
        "port: 3306  # або port: 3308 для домашнього ПК\n"
        "pass: 1:2 # : - частина паролю\n"
        "; Цей рядок є коментарем\n"
        "\n",
        encoding="utf-8"
    )
    logging.info(f"Файл создан: {path.resolve()}")


def strip_comment(line: str) -> str:
    for comment_symbol in ("#", ";"):
        if comment_symbol in line:
            line = line.split(comment_symbol, 1)[0]
    return line.strip()


def parse_ini(filename: str) -> dict | None:
    result = {}
    try:
        with open(filename, encoding="utf-8") as file:
            for raw_line in file:
                line = raw_line.strip()

                if not line:
                    result[f"__empty_{len(result)}__"] = ""
                    continue

                if line.startswith("#") or line.startswith(";"):
                    continue

                clean_line = strip_comment(line)
                if ":" not in clean_line:
                    continue

                key, value = clean_line.split(":", 1)
                result[key.strip()] = value.strip()

        return result

    except OSError as err:
        logging.error(f"Ошибка доступа к файлу: {err}")
        return None


def main() -> None:
    create_file()
    config = parse_ini(CONFIG_PATH)
    if config is not None:
        logging.info("Распарсенный конфиг:")
        for k, v in config.items():
            logging.info(f"  {k} = {v}")


if __name__ == "__main__":
    main()
