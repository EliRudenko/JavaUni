from math import gcd

class Fraction:
    def __init__(self, n, d):
        if d == 0:
            raise ValueError("Знаменник не може бути нулем")

        self.n = n
        self.d = d
        self._reduce()

    def __str__(self):
        return f"{self.n}/{self.d}"

    def _reduce(self):
        g = gcd(self.n, self.d)
        self.n //= g
        self.d //= g

        if self.d < 0:
            self.n = -self.n
            self.d = -self.d

    def add(self, other): return Fraction(self.n*other.d + other.n*self.d, self.d*other.d)
    def sub(self, other): return Fraction(self.n*other.d - other.n*self.d, self.d*other.d)
    def mul(self, other): return Fraction(self.n*other.n, self.d*other.d)

    def div(self, other):
        if other.n == 0:
            raise ZeroDivisionError("Ділити на нуль не можна")
        return Fraction(self.n*other.d, self.d*other.n)

    def to_float(self):
        return self.n / self.d

    def from_float(x: float):
        text = str(x)
        digits = len(text.split(".")[1])
        return Fraction(int(x * 10 ** digits), 10 ** digits)


def main():
    a = Fraction(4, 6)
    b = Fraction(1, 3)

    print(a)      
    print(b)      
    print(a.add(b))
    print(a.mul(b)) 
    print(a.to_float())
    print(Fraction.from_float(0.25))


if __name__ == "__main__":
    main()
