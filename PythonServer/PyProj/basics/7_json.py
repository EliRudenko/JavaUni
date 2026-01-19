####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 02: Основні типи даних Python, перетворення типів, операції з різними типами
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 03: Основні структури даних Python (кортежі, словники тощо)
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 12: Формат JSON. Перетворення Python <-> JSON, сумісність типів
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 13: Запис та відновлення JSON даних до файлів
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 02: ПРИМЕРЫ ТИПОВ И ИХ СООТВЕТСТВИЙ В JSON (NONE/BOOL/NUMBERS/STR).
# - ВОПРОС 03: СЛОВАРИ/СПИСКИ КАК БАЗОВЫЕ СТРУКТУРЫ ДЛЯ JSON-ДАННЫХ.
# - ВОПРОС 12: JSON СЕРИАЛИЗАЦИЯ/ДЕСЕРИАЛИЗАЦИЯ ПОКАЗЫВАЕТ ПРЕОБРАЗОВАНИЕ PYTHON <-> JSON.
# - ВОПРОС 13: ЧТЕНИЕ/ЗАПИСЬ JSON В ФАЙЛ ИСПОЛЬЗУЕТ ФАЙЛОВЫЕ ОПЕРАЦИИ И JSON-МОДУЛЬ.
#


# Модуль json перетворює дані між Python і форматом JSON.
import json

# JSON-рядок: приклад усіх базових типів JSON (number, string, boolean, null, array, object).
j_str = '''{
"x": 123,
"y": 1.23,
"b1": true,
"b2": false,
"n": null,
"a": [1, 2, 3, 4],
"o": {
    "f1": "Значення"
  }
}'''

# Демонстрація перетворень JSON <-> Python.
def main() -> None :
    j = json.loads(j_str)  # json.loads перетворює JSON-рядок у dict/list.
    for k in j :
        # Показуємо значення та відповідний тип у Python.
        print(f"{k}: {j[k]} ({type(j[k])})")
        #x: 123 (<class 'int'>)
        #y: 1.23 (<class 'float'>)
        #b1: True (<class 'bool'>)
        #b2: False (<class 'bool'>)
        #n: None (<class 'NoneType'>) -> JSON null
        #a: [1, 2, 3, 4] (<class 'list'>) -> JSON array
        #o: {'f1': 'Значення'} (<class 'dict'>) -> JSON object
    # json.dump записує Python-об'єкт у файл у форматі JSON.
    json.dump(j, ensure_ascii=False, indent=4, fp=open("j.json", "w", encoding='utf-8'))
    
    try:
        # json.load читає JSON із файлу і повертає Python-об'єкт.
        j2 = json.load(open("j.json", "r", encoding='utf-8'))
    except json.decoder.JSONDecodeError as err:
        print("Error read file", err)
    else:
        print(j2)


if __name__ == "__main__" :
    main()
