class Point:
    x: float = 0
    y: float = 0

def main() -> None :
    p1 = Point()
    p2 = Point()
    Point.x = 10
    print(f"p1: ({p1.x}, {p1.y})")
    print(f"p2: ({p2.x}, {p2.y})")
    p1.x = 5
    print(f"p1: ({p1.x}, {p1.y})")
    print(f"p2: ({p2.x}, {p2.y})")
    del p1.x
    print(f"p1: ({p1.x}, {p1.y})")
    print(f"p2: ({p2.x}, {p2.y})")
    


if __name__ == "__main__" :
    main()