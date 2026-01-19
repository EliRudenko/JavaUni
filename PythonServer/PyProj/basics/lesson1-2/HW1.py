def get_int(prompt, cond=lambda x: True, err_msg="Невірне число!"):
    while True:
        try:
            val = int(input(prompt))
            if cond(val):
                return val
        except ValueError:
            pass
        print(err_msg)


def main():
    print("____Генератор циклу")

    start = get_int("Введіть початкове число: ")
    end = get_int("Введіть кінцеве число: ", cond=lambda x: x != start, err_msg="Числа не повинні співпадати!")

    step = get_int("Введіть крок (залиште 0 для автопідбору): ", cond=lambda x: True)
    if step == 0:
        step = 1 if end > start else -1

    if (end - start) * step <= 0:
        print(f"Автопідбір кроку змінює крок на {'1' if end > start else '-1'}")
        step = 1 if end > start else -1

    print(f"Результат циклу: start={start}, end={end}, step={step}")
    print("____Вивід чисел ")

    for i in range(start, end + (1 if step > 0 else -1), step):
        print(i, end=' ')
    print()


if __name__ == "__main__":
    main()
