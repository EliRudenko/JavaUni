# Робота з базами даних 
# # 1. Встановлення СУБД. Використовуємо MYSQL 
# # 2. Створюємо БД та користувача для неї (подаємо команди у Workbench) 
# # CREATE DATABASE server_221; 
# # CREATE USER user_knp_221@localhost IDENTIFIED BY 'pass_221'; 
# # GRANT ALL PRIVILEGES ON server_221.* TO user_knp_221@localhost; 
# # 3. Додаємо драйвери підключення до БД


import mysql.connector
import os
import hashlib

# -----------------------------
# Настройки базы данных
# -----------------------------
db_ini = {
    'host': 'localhost',
    'port': 3306,
    'user': 'user_student',
    'password': 'pass_221',
    'database': 'server_221',
    'charset': 'utf8mb4'
}

db_connection = None

def simple_hash(password):
    # создание хеш пароля с salt
    salt = os.urandom(16)  # случайная соль
    key = hashlib.pbkdf2_hmac('sha256', password.encode(), salt, 1000)
    return salt.hex() + "$" + key.hex()

def simple_verify(stored, password):
    salt_hex, key_hex = stored.split('$')
    salt = bytes.fromhex(salt_hex)
    key = hashlib.pbkdf2_hmac('sha256', password.encode(), salt, 1000)
    return key.hex() == key_hex

def connect_db():
    global db_connection
    try:
        db_connection = mysql.connector.connect(**db_ini)
        print("Подключение к БД успешно!")
    except mysql.connector.Error as err:
        print("Ошибка подключения:", err)

def close_connection():
    if db_connection:
        db_connection.close()
        print("Соединение закрыто.")

def create_user(username, password):
    # Создаёт пользователя с хешированным паролем
    hashed_pwd = simple_hash(password)
    try:
        cursor = db_connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE,
                password VARCHAR(255)
            )
        """)
        cursor.execute(
            "INSERT INTO users (username, password) VALUES (%s, %s)",
            (username, hashed_pwd)
        )
        db_connection.commit()
        print(f"Пользователь '{username}' создан с хешированным паролем.")
    except mysql.connector.Error as err:
        print("Ошибка:", err)
    finally:
        cursor.close()

def check_user(username, password):
    # проверка правильности пароля
    try:
        cursor = db_connection.cursor()
        cursor.execute("SELECT password FROM users WHERE username = %s", (username,))
        row = cursor.fetchone()
        if row and simple_verify(row[0], password):
            print(f"Пароль для '{username}' верный!")
        else:
            print(f"Неправильное имя пользователя или пароль для '{username}'.")
    except mysql.connector.Error as err:
        print("Ошибка:", err)
    finally:
        cursor.close()

def main():
    connect_db()

    create_user("student1", "my_secret_pass")

    check_user("student1", "my_secret_pass")   # верный пароль
    check_user("student1", "wrong_pass")       # неверный пароль

    close_connection()

if __name__ == "__main__":
    main()
