def create_file() -> None :
    filename = 'db.ini'
    file = None
    try :
        file=open(filename, mode="w",encoding='utf-8')
        file.write("Дані для підключення БД")
        file.write('host: localhost\r\nport: 3306')
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
                    k, v = line.split(':')
                    ret[k.strip()] =v.strip()
                
        return ret
    except OSError as err :
        print("Error read file", err)
        return None  


def parse_ini(filename:str) -> dict|None :
    try :
        with open(filename, encoding='utf-8') as file :
            return {k:v for k, v in (map(str.strip, line.split(':')) for line in file if ':' in line)}
        
    except OSError as err :
        print("Error read file", err)
        return None  


def main() -> None :
    #create_file()
      print(parse_ini_imp("db.ini"))


if __name__ == '__main__' :
    main()
