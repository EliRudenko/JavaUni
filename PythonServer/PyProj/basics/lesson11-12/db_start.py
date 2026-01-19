import mysql.connector
from mysql.connector import Error

db_ini = {
    'host': 'localhost',
    'port': 3306,
    'user': 'user_knp_221',
    'password': 'pass_221',
    'database': 'server_221',
    'charset': 'utf8mb4',
    'use_unicode': True,
    'collation': 'utf8mb4_general_ci'
}


def connect_db():
    try:
        connection = mysql.connector.connect(**db_ini)
        if connection.is_connected():
            print("Успешное подключение к MySQL")
            with connection.cursor() as cursor:
                cursor.execute("SELECT DATABASE();")
                db_name = cursor.fetchone()
                print("Текущая база данных:", db_name[0])
        return connection
    except Error as e:
        print("Ошибка подключения:", e)
        return None


def show_databases(connection):
    if not connection or not connection.is_connected():
        print("Нет подключения")
        return
    try:
        with connection.cursor() as cursor:
            cursor.execute("SHOW DATABASES;")
            databases = cursor.fetchall()

            print("\nСписок доступных баз данных:")
            for db in databases:
                print(" -", db[0])
    except Error as e:
        print("Ошибка при получении списка баз данных:", e)


def show_uuid(connection):
    sql = """
        SELECT UUID(), UUID()
        UNION ALL
        SELECT UUID(), UUID()
        UNION ALL
        SELECT UUID(), UUID();
    """
    if not connection:
        print("Нет подключения")
        return
    try:
        with connection.cursor() as cursor:
            cursor.execute(sql)
            print("\nРезультат show_uuid():")
            for row in cursor:
                print(row)
    except Error as e:
        print("Ошибка при выполнении show_uuid:", e)
        print(sql)


def show_uuid2(connection):
    sql = "SELECT UUID() AS u1, UUID() AS u2"
    if not connection:
        print("Нет подключения")
        return
    try:
        with connection.cursor(dictionary=True) as cursor:
            cursor.execute(sql)
            print("\nРезультат show_uuid2():")
            for row in cursor:
                print(row)
    except Error as e:
        print("Ошибка show_uuid2:", e)
        print(sql)


def show_prep(connection):
    sql = "SELECT DATEDIFF(CURRENT_TIMESTAMP, %s)"
    if not connection:
        print("Нет подключения")
        return
    try:
        with connection.cursor(prepared=True) as cursor:
            cursor.execute(sql, ('2025-10-01',))
            print("\nРезультат show_prep():")
            print(cursor.column_names)
            print('------------------')
            for row in cursor:
                print(row)
    except Error as e:
        print("Ошибка show_prep:", e)
        print(sql)


def main():
    connection = connect_db()
    if connection:
        show_databases(connection)
        show_uuid(connection)
        show_uuid2(connection)
        show_prep(connection)
        connection.close()
        print("\nСоединение закрыто")


if __name__ == "__main__":
    main()
