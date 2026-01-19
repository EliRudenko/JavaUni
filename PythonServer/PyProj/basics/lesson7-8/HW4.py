from __future__ import annotations
import math
import logging

logging.basicConfig(
    level=logging.INFO,
    format="%(levelname)s: %(message)s"
)


class Fraction:
    def __init__(self, numerator: int, denominator: int) -> None:
        if not isinstance(numerator, int) or not isinstance(denominator, int):
            raise TypeError("Числитель и знаменатель должны быть целыми числами.")
        if denominator == 0:
            raise ValueError("Знаменатель не может быть равен нулю.")
        if denominator < 0:
            numerator, denominator = -numerator, -denominator

        gcd = math.gcd(numerator, denominator)
        self.numerator = numerator // gcd
        self.denominator = denominator // gcd
        logging.info(f"Создана дробь: {self}")

    def __repr__(self) -> str:
        return f"Fraction({self.numerator}, {self.denominator})"

    def __str__(self) -> str:
        return f"{self.numerator}/{self.denominator}"

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Fraction):
            return NotImplemented
        return (
            self.numerator == other.numerator
            and self.denominator == other.denominator
        )

    def __add__(self, other: Fraction) -> Fraction:
        if not isinstance(other, Fraction):
            return NotImplemented
        num = self.numerator * other.denominator + other.numerator * self.denominator
        den = self.denominator * other.denominator
        return Fraction(num, den)

    def __sub__(self, other: Fraction) -> Fraction:
        if not isinstance(other, Fraction):
            return NotImplemented
        num = self.numerator * other.denominator - other.numerator * self.denominator
        den = self.denominator * other.denominator
        return Fraction(num, den)

    def __mul__(self, other: Fraction) -> Fraction:
        if not isinstance(other, Fraction):
            return NotImplemented
        return Fraction(
            self.numerator * other.numerator,
            self.denominator * other.denominator
        )

    def __truediv__(self, other: Fraction) -> Fraction:
        if not isinstance(other, Fraction):
            return NotImplemented
        if other.numerator == 0:
            raise ZeroDivisionError("Деление на нулевую дробь.")
        return Fraction(
            self.numerator * other.denominator,
            self.denominator * other.numerator
        )

    def to_float(self) -> float:
        return self.numerator / self.denominator

    @staticmethod
    def from_float(value: float, max_denominator: int = 1000000) -> Fraction:
        if not isinstance(value, (int, float)):
            raise TypeError("Ожидается float или int.")
        frac = Fraction(*value.as_integer_ratio())
        if frac.denominator > max_denominator:
            factor = frac.denominator // max_denominator + 1
            frac = Fraction(frac.numerator // factor, frac.denominator // factor)
        return frac

    def __neg__(self) -> Fraction:
        return Fraction(-self.numerator, self.denominator)

    def __abs__(self) -> Fraction:
        return Fraction(abs(self.numerator), self.denominator)

    def as_tuple(self) -> tuple[int, int]:
        return self.numerator, self.denominator


if __name__ == "__main__":
    f1 = Fraction(4, 6)
    f2 = Fraction(3, 8)

    print("f1 =", f1)
    print("f2 =", f2)

    print("Сумма:", f1 + f2)
    print("Разность:", f1 - f2)
    print("Произведение:", f1 * f2)
    print("Деление:", f1 / f2)

    print("Как float:", (f1 / f2).to_float())

    f3 = Fraction.from_float(0.75)
    print("Из float (0.75):", f3)

    print("Абсолютное значение:", abs(Fraction(-10, 4)))
