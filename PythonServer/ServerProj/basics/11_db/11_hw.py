import mysql.connector
from datetime import datetime

# Налаштування підключення до MySQL
db_ini = {
    'host' : 'localhost',
    'port' : 3306,
    'user' : 'user_student',
    'password' : 'pass_221',
    'database' : 'server_221',
    'charset' : 'utf8mb4',
    'use_unicode' : True,
}

# Функція підключення
def connect_db():
    try:
        return mysql.connector.connect(**db_ini)
    except mysql.connector.Error as err:
        print("Помилка підключення:", err)
        return None

# Перевірка формату дати (YYYY-MM-DD)
def validate_date(date_text):
    try:
        return datetime.strptime(date_text, "%Y-%m-%d")
    except ValueError:
        raise ValueError("Неправильний формат дати. Використовуйте YYYY-MM-DD")

def check_date_difference(db_conn, user_date):
    sql = "SELECT DATEDIFF(CURRENT_DATE, %s)"
    cursor = db_conn.cursor(prepared=True)
    cursor.execute(sql, (user_date,))
    diff = cursor.fetchone()[0]
    cursor.close()
    return diff

def main():
    db_conn = connect_db()
    if not db_conn:
        return

    user_input = input("Введіть дату (YYYY-MM-DD): ").strip()
    
    # Валідація
    user_date_obj = validate_date(user_input)
    user_date_str = user_date_obj.strftime("%Y-%m-%d")  # для передачі в SQL

    diff_days = check_date_difference(db_conn, user_date_str)

    if diff_days > 0:
        print(f"Дата у минулому за {diff_days} днів від поточної дати")
    elif diff_days < 0:
        print(f"Дата у майбутньому через {abs(diff_days)} днів від поточної дати")
    else:
        print("Дата є поточною")

    db_conn.close()

if __name__ == "__main__":
    main()
