# Генератори: функції з yield, що повертають ітератор.

def countdown(n: int):
    # Генератор, який повертає числа від n до 0.
    # Кожен yield "заморожує" стан функції до наступної ітерації.
    while n >= 0:
        yield n  # Повертаємо поточне значення і призупиняємо виконання.
        n -= 1   # Зменшуємо лічильник перед наступним yield.


def squares(limit: int):
    # Генератор квадратів від 0 до limit-1.
    for i in range(limit):
        yield i * i  # Повертаємо значення квадрата.


def main() -> None:
    # Ітерація генератора через for.
    for value in countdown(3):
        print("countdown:", value)

    # Ручна ітерація генератора через next().
    gen = squares(4)
    print("first:", next(gen))
    print("second:", next(gen))

    # Перетворення генератора у список.
    print("all squares:", list(squares(5)))


if __name__ == "__main__":
    main()
