y = 10
lamg1 = None

def init_lamg1() -> None:
    global lamg1
    w = 30
    lamg1 = lambda x: print("x=%d, y=%d, w=%d" % (x, y, w))


def main():
    lam1 = lambda x: print(x)
    lam1("Hello from lambda")
    lam2 = lambda x, y: print(x, y)
    lam2(5, 10)
    lam3 = lambda: print("No parameters")
    lam3()
    (lambda x, y: print("Sum is", x + y))(3, 7)

    init_lamg1()
    lamg1(100)


if __name__ == "__main__" :
    main()