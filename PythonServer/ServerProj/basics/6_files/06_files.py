# Робота за файлами
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
        print("----------EOF--------")
    finally :
        if file != None :
            file.close()

            
def read_as_string(filename:str) -> str :
    try :
        with open(filename, encoding='utf-8') as file :  # with - автозакриття ресурсів
            return file.read()
    except OSError as err :
        print("Error read file", err)
        return None  
    # finally -- не потрібно через блок with

   
def parse_ini_imp(filename:str) -> dict|None :          # _imp - imperative style
    ret = {}
    try :
        with open(filename, encoding='utf-8') as file :
            for line in file :                              # файл ітеруєтсья по рядках (по розривах рдків)
                if ':' in line :                            # ~line.contains(':')
                    k, v = line.split(':')
                    ret[k.strip()] = v.strip()              # strip - аналог trim
                
        return ret
    except OSError as err :
        print("Error read file", err)
        return None  


def parse_ini(filename:str) -> dict|None :
    try :
        with open(filename, encoding='utf-8') as file :
            return {k:v for k, v in (                       # генератор dict
                map(                                        # map - прикладання функції до послідовності
                    str.strip,                              # функція, що прикладається
                    line.split(':', 1)                      # множина (1 - максимальна кількість ділень (актів ділень))
                )                                           #
                for line in file                            # генератор множини для послідовності інструкції
                    if ':' in line                          # фільтр генератора (скорочений тернарний вираз)
            )}
        
    except OSError as err :
        print("Error read file", err)
        return None  


def main() -> None :
    #create_file()
    #print_file("db.ini")
    #print(read_as_string("db.ini"))
    # print(parse_ini_imp("db.ini"))
    print(parse_ini("db.ini"))


if __name__ == '__main__' :
    main()
