def throws() -> None:
    print("Throws error")
    raise TypeError


def throw_with_msg() -> None:
    raise ValueError("This is a ValueError")


def not_throws() -> None:
    pass


def main() -> None:
    try:
        throws()
        return
    except:
        print("Caught an error")
        
    try:
        throw_with_msg()
    except TypeError:
        print("Caught a TypeError")
    except ValueError as e:
        print(e)
    except Exception as e:
        print(f"Caught a generic exception: {e}")
    finally:
        print("Finally block executed")

    try:
        not_throws()
    except:
        print("This will not be printed")
    else:
        print("Continue")
    finally:
        print("Finally")



if __name__ == "__main__":
    main()