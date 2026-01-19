import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


"""
# ФУНКЦИИ
x = 0

def set_x(new_value):
    global x
    if not isinstance(new_value, int):
        raise TypeError(f"Expected int, got {type(new_value).__name__}")
    logging.info(f"x изменено с {x} на {new_value}")
    x = new_value


def get_x():
    logging.debug(f"x = {x}")
    return x


def increment(step=1):
    global x
    logging.info(f"+ к x на {step}")
    x += step


def reset():
    global x
    logging.info("x в 0")
    x = 0




# Пример использования
if __name__ == "__main__":
    set_x(10)
    print("Initial x:", get_x())

    set_x(20)
    print("Updated x:", get_x())

    increment(5)
    print("After increment:", get_x())

    reset()
    print("After reset:", get_x())
"""


"""
# РЕКУРСИЯ
def show(x: int, indent: int = 0) -> int:
    logging.info(f"{'  ' * indent}Вход: x={x}")
    print(f"{'  ' * indent}{x}")

    total = x
    if x > 0:
        total += show(x - 1, indent + 1)
        total += show(x - 2, indent + 1)
    else:
        print(f"{'  ' * indent}Базовый случай уже")

    logging.info(f"{'  ' * indent}Выход: x={x}, total={total}")
    return total


result = show(3)
print(f"Сумма всех значений: {result}")
"""






# ЛЯМБДА

from functools import reduce
"""
def main():
    print((lambda x: (lambda y: x * y))(3)(4))

    nums = [1, 2, 3, 4, 5]
    print(list(map(lambda x: x**2, nums)))
    print(list(filter(lambda x: x % 2 == 0, nums)))
    print(reduce(lambda a, b: a + b, nums))

    factorial = lambda n: 1 if n == 0 else n * factorial(n - 1)
    print(factorial(5))

if __name__ == "__main__":
    main()
"""

