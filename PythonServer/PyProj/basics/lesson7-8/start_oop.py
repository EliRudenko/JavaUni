from __future__ import annotations
import math

class Point:
    def __init__(self, x: float = 0.0, y: float = 0.0) -> None:
        self.x = float(x)
        self.y = float(y)

    def __repr__(self) -> str:
        return f"Point(x={self.x}, y={self.y})"

    def __str__(self) -> str:
        return f"({self.x:.2f}, {self.y:.2f})"

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Point):
            return NotImplemented
        return math.isclose(self.x, other.x) and math.isclose(self.y, other.y)

    def __add__(self, other: Point) -> Point:
        if not isinstance(other, Point):
            return NotImplemented
        return Point(self.x + other.x, self.y + other.y)

    def __mul__(self, scalar: float) -> Point:
        if not isinstance(scalar, (int, float)):
            return NotImplemented
        return Point(self.x * scalar, self.y * scalar)

    def distance_to(self, other: Point) -> float:
        if not isinstance(other, Point):
            raise TypeError("distance_to ожидает объект Point")
        return math.hypot(self.x - other.x, self.y - other.y)

    def move(self, dx: float = 0.0, dy: float = 0.0) -> None:
        self.x += dx
        self.y += dy

    def as_tuple(self) -> tuple[float, float]:
        return self.x, self.y

    def magnitude(self) -> float:
        return (self.x**2 + self.y**2) ** 0.5




if __name__ == "__main__":
    p1 = Point(3, 4)
    p2 = Point(1, 2)

    print("Точка 1:", p1)
    print("Точка 2:", p2)
    print("Расстояние между точками:", p1.distance_to(p2))

    p3 = p1 + p2  # Используем __add__
    print("Сумма точек:", p3)
    print("Магнитуда суммы:", p3.magnitude())

    p1.move(-1, 2)
    print("После сдвига:", p1)
    print("Магнитуда точки 1:", p1.magnitude())

    p4 = p1 * 2
    print("Умножение точки 1 на 2:", p4)
    print("Магнитуда умноженной точки:", p4.magnitude())