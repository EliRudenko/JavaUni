
"""
print("Hello World")

x = 20

print(int(x) + 5)        # 25
print(str(x) + str(5))   # "205"

x = 2 + 3j
print(x, type(x))        # (2+3j) <class 'complex'>

print(2 ** 256)          # очень большое целое число
print(2.0 ** 256)        # число с плавающей точкой


x = 10
y = 20

s = f"x = {x}, y = {y}"      # інтерполяція, імперативний стиль
print(s)

s = "x = %d, y = %d" % (x, y)   # інтерполяція, кортежний стиль
print(s)

"""





"""
# Операнды
a = 10
b = 3
c = True
d = False
lst = [1, 2, 3]

print("_______Арифм.")
print("a + b =", a + b)
print("a - b =", a - b)
print("a * b =", a * b)
print("a / b =", a / b)   # деление (результат float)
print("a // b =", a // b)
print("a % b =", a % b)   # остаток от деления
print("a ** b =", a ** b)

print("\n_________сравнения")
print("a == b:", a == b)
print("a != b:", a != b)
print("a > b:", a > b)
print("a < b:", a < b)
print("a >= b:", a >= b)
print("a <= b:", a <= b)

print("\n______Логические")
print("c and d:", c and d)
print("c or d:", c or d)
print("not c:", not c)

print("\n_______Побитовые")
print("a & b =", a & b)   # побитовое И
print("a | b =", a | b)   # побитовое ИЛИ
print("a ^ b =", a ^ b)   #  XOR
print("~a =", ~a)         # НЕ (инверсия)
print("a << 1 =", a << 1) #сдвиг влево
print("a >> 1 =", a >> 1) #двиг вправо

print("\n_____________присваивания")
x = 5
print("x =", x)
x += 2   # x = x + 2
print("x += 2 →", x)
x -= 1   # x = x - 1
print("x -= 1 →", x)
x *= 3
print("x *= 3 →", x)
x /= 2
print("x /= 2 →", x)
x //= 2
print("x //= 2 →", x)
x %= 2
print("x %= 2 →", x)
x **= 3
print("x **= 3 →", x)

print("\n____операторы идентичности")
print("c is d:", c is d)         # проверка идентичности (один объект?)
print("c is not d:", c is not d)

print("\n____принадлежности")
print("2 in lst:", 2 in lst)     # элемент содержится в списке
print("5 not in lst:", 5 not in lst) # элемент отсутствует

"""




"""

print("\n____________")

age = 20
salary = 3000
has_experience = True
city = "Odesa"

if (20 <= age < 40) and (salary >= 2500 or has_experience) and (city in ["Odesa", "Lviv"]) and not (age == 30 and salary < 2000):
    print("кандидат підходить")
else:
    print("Умова НЕ виконується")

"""








numbers = range(1, 6)

even_multiples = {
    n: [n * m for m in numbers if (n * m) % 2 == 0]
    for n in numbers
}

for n, evens in even_multiples.items():
    print(f"{n} Чётные произведения: {evens}, Сумма: {sum(evens)}")

total_sum = sum(sum(evens) for evens in even_multiples.values())
print(f"\nОбщая сумма: {total_sum}")





"""
# генератор

r = range(10)
print(r)
print(list(r))

"""
