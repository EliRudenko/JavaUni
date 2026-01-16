def print_hello() -> None :
    print("Hello")


def print_hello(x:str) -> None :
    print("Hi", x)


def print_hello(x:str='') -> None :
    print("Hola", x)


def greet(phrase:str='Hello', who:str='All') -> None :
    print(phrase, who)

x = 10

def get_x() -> int :
    return x


def set_x(new_x:int) -> None :
    x = new_x 
    print ("X set to", x)


def set_global_x(new_x:int) -> None :
    global x
    x = new_x


def show(x:int) -> None :
    print(x, end =' ')
    if x > 0 :
        show(x - 1)
    else:
        print()


def main() -> None :
    # show(10)
    print('X =' , get_x())
    set_x(20)
    print('X =' , get_x())
    # set_global_x(20)
    # print('X =' , get_x())
    # print_hello()
    # print_hello("World")
    # greet()
    # greet(who="Everyone")

if __name__ == "__main__" :
    main()