import json
from pathlib import Path


J_PATH = Path("j.json")

J_STR = """
{
    "x": 123,
    "y": 1.23,
    "b1": true,
    "b2": false,
    "b3": true,
    "n": null,
    "a": [1, 2, 3],
    "o": {
        "x": 123,
        "fi": "value"
    }
}
"""


def dump_json(data: dict, path: Path) -> None:
    with path.open("w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=4)


def load_json(path: Path) -> dict:
    if not path.exists():
        raise FileNotFoundError(f"Файл {path} не найден")

    with path.open("r", encoding="utf-8") as f:
        try:
            return json.load(f)
        except json.JSONDecodeError as err:
            raise ValueError(f"Ошибка чтения JSON: {err}") from err


def main() -> None:
    data = json.loads(J_STR)

    print(f"Тип: {type(data)}\nСодержимое:")
    for key, value in data.items():
        print(f"  {key:<3} → {type(value).__name__}: {value}")

    json_str = json.dumps(data, ensure_ascii=False)
    print(f"\nJSON как строка:\n{json_str}")

    dump_json(data, J_PATH)
    print(f"\nДанные сохранены в {J_PATH.resolve()}")

    try:
        loaded = load_json(J_PATH)
        print("\nПрочитано из файла:")
        for k, v in loaded.items():
            print(f"  {k}: {v}")
    except (FileNotFoundError, ValueError) as e:
        print(f"{e}")


if __name__ == "__main__":
    main()
