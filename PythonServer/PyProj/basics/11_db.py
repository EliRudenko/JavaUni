#-- CREATE DATABASE server_221;
#-- CREATE USER user_221@localhost IDENTIFIED BY 'pass_221';
#-- GRANT ALL PRIVILEGES ON server_221.* TO user_221@localhost;
import mysql.connector
from datetime import datetime

db_ini = {
    'host': 'localhost',
    'port': 3306,
    'user': 'user_221',
    'password': 'pass_221',
    'database': 'server_221',
    'charset': 'utf8mb4',
    'collation': 'utf8mb4_unicode_ci',
    'use_unicode': True
}

db_connection = None

def connect_db():
    global db_connection
    try:
        db_connection = mysql.connector.connect(**db_ini)
    except mysql.connector.Error as err:
        print(f"Помилка підключення до бази даних: {err}")
    else:
        print("Підключення до бази даних успішне.")


def show_databases():
    global db_connection

    if db_connection is None:
        print("Немає підключення до бази даних.")
        return
    try:
        cursor = db_connection.cursor()                     
        cursor.execute("SHOW DATABASES")                     
    except mysql.connector.Error as err:                     
        print(f"Помилка виконання запиту: {err}")                     
    else:
        print(cursor.column_names)
        print('--------------------')
        for row in cursor:
            print(row)
    finally:
        cursor.close()


def show_uuids():
    global db_connection

    if db_connection is None:
        print("Немає підключення до бази даних.")
        return
    try:
        cursor = db_connection.cursor(dictionary=True)                     
        sql = "select uuid() as u1, uuid() as u2 union all select uuid()," \
        " uuid() union all select uuid(), uuid()"                  
        cursor.execute(sql)                     
    except mysql.connector.Error as err:                     
        print(f"Помилка виконання запиту: {err}")                     
    else:
        print(cursor.column_names)
        print('--------------------')
        for row in cursor:
            print(row)
    finally:
        cursor.close()

    
def show_uuids2():
    global db_connection

    if db_connection is None:
        print("Немає підключення до бази даних.")
        return
    try:
        cursor = db_connection.cursor(dictionary=True)                     
        sql = "select uuid(), uuid() union all select uuid()," \
        " uuid() union all select uuid(), uuid()"                  
        cursor.execute(sql)                     
    except mysql.connector.Error as err:                     
        print(f"Помилка виконання запиту: {err}")                     
    else:
        print(cursor.column_names)
        print('--------------------')
        for row in cursor:
            print(row)
    finally:
        cursor.close()


def show_prep():
    global db_connection

    if db_connection is None:
        print("Немає підключення до бази даних.")
        return
    try:
        sql = "select datediff(current_timestamp, %s)"                  
        with db_connection.cursor(prepared=True, dictionary=True) as cursor:                     
            cursor.execute(sql, ('2024-01-01',))     
            print(cursor.column_names)
            print('--------------------')
            for row in cursor:
                print(row)                     
    except mysql.connector.Error as err:                     
        print(f"Помилка виконання запиту: {err}")                     
 

def validate_date(date_string):
    try:
        datetime.strptime(date_string, '%Y-%m-%d')
        return True
    except ValueError:
        return False
    

def show_datediff():
    global db_connection

    if db_connection is None:
        print("Немає підключення до бази даних.")
        return
    
    user_date = input("Введіть дату (формат YYYY-MM-DD): ").strip()
    
    if not validate_date(user_date):
        print("Помилка: Невірний формат дати. Використовуйте формат YYYY-MM-DD")
        return
    
    try:
        sql = "SELECT DATEDIFF(CURRENT_DATE, %s) AS diff"
        
        with db_connection.cursor(prepared=True, dictionary=True) as cursor:
            cursor.execute(sql, (user_date,))
            for row in cursor:
                diff = row['diff']
                if diff > 0:
                    print(f"Дата у минулому за {diff} днів від поточної дати")
                elif diff < 0:
                    print(f"Дата у майбутньому через {abs(diff)} дні(днів) від поточної дати")
                else:
                    print("Дата є поточною")
    except mysql.connector.Error as err:
        print(f"Помилка виконання запиту (валідація СУБД): {err}")

def close_connection():
    if(db_connection is not None and db_connection.is_connected()):
        db_connection.close()


def main():
    connect_db()
    show_databases()
    print('--------------------')
    show_uuids()
    print('--------------------')
    show_uuids2()
    print('--------------------')
    show_prep()
    print('--------------------')
    show_datediff()

    close_connection()


if __name__ == "__main__":
    main()