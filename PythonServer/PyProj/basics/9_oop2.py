class Point:
    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y

    def __str__(self):
        return f"Point({self.x}, {self.y})"
    
    def __repr__(self):
        return f"Point({self.x}, {self.y})"
    
    def magnitude(self) -> float:
        return (self.x ** 2 + self.y ** 2) ** 0.5
    
    def __add__(self, other):
        if isinstance(other, Point):
            return Point(self.x + other.x, self.y + other.y)
        raise TypeError("Operand must be an instance of Point")
    
    def __mul__(self, scalar: float):
        if isinstance(scalar, (int, float)):
            return Point(self.x * scalar, self.y * scalar)
        elif isinstance(scalar, Point):
            return self.x * scalar.x + self.y * scalar.y
        raise TypeError("Operand must be a number or an instance of Point")
        

def main() -> None :
    p1 = Point(3, 4)
    p2 = Point(2, 5)

    print(p1)
    print(p1.magnitude())
    print(p1 + p2)


if __name__ == "__main__" :
    main()