# Питання 12. Формат JSON. Перетворення Python <-> JSON, сумісність типів

## Коротка відповідь
JSON — текстовий формат для обміну даними. Python має модуль `json`, який перетворює JSON-рядки в Python-об’єкти і навпаки. Типи мапляться так: JSON `null` -> Python `None`, `true/false` -> `bool`, `number` -> `int/float`, `array` -> `list`, `object` -> `dict`.

## Детально
- **JSON типи:** object, array, string, number, boolean, null.
- **Відповідність Python:**
  - `object` -> `dict`
  - `array` -> `list`
  - `string` -> `str`
  - `number` -> `int` або `float`
  - `true/false` -> `bool`
  - `null` -> `None`
- **Перетворення:**
  - `json.loads()` — JSON рядок -> Python
  - `json.dumps()` — Python -> JSON рядок

## Де в коді
- `basics/7_json.py` — демонстрація типів і перетворень між JSON та Python.

## Куди перейти в коді
- **`basics/7_json.py`** — приклад JSON-рядка та відповідних типів у Python.
