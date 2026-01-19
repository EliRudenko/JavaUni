# Клас Point демонструє ООП: конструктор, методи, спеціальні методи.
class Point:
    # __init__ викликається під час створення об'єкта.
    def __init__(self, x=0, y=0):
        self.x = x  # Поле об'єкта.
        self.y = y  # Поле об'єкта.

    # __str__ задає "людське" представлення об'єкта.
    def __str__(self):
        return f"Point({self.x}, {self.y})"
    
    # __repr__ задає "технічне" представлення (зручно для дебагу).
    def __repr__(self):
        return f"Point({self.x}, {self.y})"

    # Статичний метод: належить класу, не використовує self.
    @staticmethod
    def origin():
        return Point(0, 0)

    # Звичайний метод класу.
    def magnitude(self) -> float:
        return (self.x ** 2 + self.y ** 2) ** 0.5  # Довжина вектора.
    
    # Перевантаження оператора + для власного типу.
    def __add__(self, other):
        if isinstance(other, Point):
            return Point(self.x + other.x, self.y + other.y)
        raise TypeError("Operand must be an instance of Point")
    
    # Перевантаження оператора * для множення на число або скалярний добуток.
    def __mul__(self, scalar: float):
        if isinstance(scalar, (int, float)):
            return Point(self.x * scalar, self.y * scalar)
        elif isinstance(scalar, Point):
            return self.x * scalar.x + self.y * scalar.y
        raise TypeError("Operand must be a number or an instance of Point")
        

def main() -> None :
    p1 = Point(3, 4)  # Створення об'єкта.
    p2 = Point(2, 5)  # Створення об'єкта.

    print(p1)             # __str__
    print(p1.magnitude()) # Звичайний метод.
    print(p1 + p2)        # __add__
    print(Point.origin()) # Виклик статичного методу.


if __name__ == "__main__" :
    main()
