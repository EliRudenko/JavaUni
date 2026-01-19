# Основні типи даних та структури у Python.

def main() -> None:
    # Прості (скалярні) типи.
    i = 42            # int: цілі числа.
    f = 3.14          # float: числа з плаваючою комою.
    s = "text"        # str: рядок.
    b = True          # bool: логічний тип.
    n = None          # NoneType: відсутність значення.

    # Перетворення типів (casting).
    i_from_str = int("10")       # "10" -> 10
    f_from_int = float(i)        # 42 -> 42.0
    s_from_bool = str(b)         # True -> "True"
    b_from_int = bool(0)         # 0 -> False

    # Операції з різними типами (неявні правила).
    sum_number = i + f           # int + float -> float
    concat = s + "!"             # str + str -> str
    repeated = s * 3             # str * int -> повторення

    # Основні структури даних.
    lst = [1, 2, 3]              # list: змінювана послідовність.
    tpl = (1, 2, 3)              # tuple: незмінювана послідовність.
    st = {1, 2, 3}               # set: множина унікальних значень.
    dct = {"a": 1, "b": 2}       # dict: ключ-значення.

    # Приклад ітерації по структурах.
    for item in lst:             # for-цикл.
        print("list item:", item)
    for key, value in dct.items():
        print("dict item:", key, value)

    # Розгалуження (if/elif/else).
    if i > 0:
        print("positive")
    elif i == 0:
        print("zero")
    else:
        print("negative")

    # Вивід результатів, щоб продемонструвати типи.
    print(type(i), type(f), type(s), type(b), type(n))
    print(i_from_str, f_from_int, s_from_bool, b_from_int)
    print(sum_number, concat, repeated)
    print(lst, tpl, st, dct)


if __name__ == "__main__":
    main()
