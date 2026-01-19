import mysql.connector
from mysql.connector import Error
from datetime import datetime

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

def show_uuid_table(connection):
    sql = """
        SELECT UUID() AS u1, UUID() AS u2
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
            print("\nРезультат запроса UUID:")
            print(f"{'UUID1':<40} | {'UUID2':<40}")
            print("-" * 85)
            for u1, u2 in cursor:
                print(f"{u1:<40} | {u2:<40}")
    except Error as e:
        print("Ошибка при выполнении запроса UUID:", e)

def diff_with_date(connection):
    while True:
        user_input = input("\nВведите дату (YYYY-MM-DD): ")
        try:
            # Проверка формата даты
            date_obj = datetime.strptime(user_input, '%Y-%m-%d').date()
            break
        except ValueError:
            print("Неправильный формат даты. Попробуйте снова.")

    sql = "SELECT DATEDIFF(CURRENT_DATE, %s);"
    try:
        with connection.cursor(prepared=True) as cursor:
            cursor.execute(sql, (user_input,))
            diff_days = cursor.fetchone()[0]

            if diff_days > 0:
                print(f"Дата в прошлом на {diff_days} днів від поточної дати")
            elif diff_days < 0:
                print(f"Дата в майбутньому через {abs(diff_days)} днів від поточної дати")
            else:
                print("Дата є поточною")
    except Error as e:
        print("Ошибка при вычислении разницы дат:", e)

def main():
    connection = connect_db()
    if connection:
        show_uuid_table(connection)
        diff_with_date(connection)
        connection.close()
        print("\nСоединение закрыто")

if __name__ == "__main__":
    main()
