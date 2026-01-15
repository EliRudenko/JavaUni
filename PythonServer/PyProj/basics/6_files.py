import os

def create_file() -> None :
    filename = 'db.ini'
    file = None
    try :
        if os.path.exists(filename) and os.path.getsize(filename) > 0:
            print(f"File '{filename}' already exists and is not empty. Skipping creation.")
            return
        file = open(filename, mode="w", encoding='utf-8')
        file.write("Дані для підключення БД\n")
        file.write('host: localhost\n')
        file.write('port: 3306')
        file.flush()
    except OSError as err :
        print("Error writing file", err)
    else :
        print("File write ok")
    finally :
        if file != None :
            file.close()


def print_file(filename:str) -> None :
    file = None
    try :
        file = open(filename, encoding='utf-8')
        print(file.read())
    except OSError as err :
        print("Error read file", err)
    else :
        print("----------EDF--------")
    finally :
        if file != None :
            file.close()

            
def read_as_string(filename:str)->str :
    try :
        with open(filename, encoding='utf-8') as file :
            return file.read()
    except OSError as err :
        print("Error read file", err)
        return None  

   
def parse_ini_imp(filename:str) -> dict|None :
    ret={}
    try :
        with open(filename, encoding='utf-8') as file :
            for line in file :
                if ':' in line :
                    k, v = line.split(':', 1)
                    ret[k.strip()] =v.strip()
                
        return ret
    except OSError as err :
        print("Error read file", err)
        return None  


def parse_ini(filename: str) -> dict | None:
    try:
        with open(filename, encoding='utf-8') as file:
            result = {}
            for line in file:
                line = line.strip()
                if not line or line.startswith('#') or line.startswith(';'):
                    continue
                if ':' in line:
                    key, value = map(str.strip, line.split(':', 1))
                    key, value = map(str.strip, line.split(':', 1))
                    for comment_char in ['#', ';']:
                        value = value.split(comment_char, 1)[0].strip()
                    result[key] = value
            return result
    except OSError as err:
        print("Error read file", err)
        return None


def main() -> None :
    create_file()
    print_file("db.ini")
    print(read_as_string("db.ini"))
    print(parse_ini_imp("db.ini"))
    print(parse_ini("db.ini"))


if __name__ == '__main__' :
    main()
