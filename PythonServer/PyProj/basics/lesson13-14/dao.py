import mysql.connector
import json
import sys
import inspect
import hashlib
from datetime import datetime, timedelta


class DataAccess:
    def __init__(self, ini_file="db.json"):
        try:
            with open(ini_file, encoding="utf-8-sig") as file:
                self.db_config = json.load(file)
        except OSError as err:
            raise RuntimeError(f"Ошибка чтения файла конфигурации: {err}")

        try:
            self.db_connection = mysql.connector.connect(**self.db_config)
            print("Подключение успешно")
        except mysql.connector.Error as err:
            print("Ошибка подключения:", err)
            raise RuntimeError(err)

    def install(self):
        self._install_users()
        self._install_roles()
        self._install_user_access()
        self._install_tokens()
        print("\nУстановка завершена\n")
        self.show_users()
        self.show_roles()
        self.show_user_access()
        self.show_tokens()

    # ---------- USERS ----------
    def _install_users(self):
        cursor = self.db_connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS users_table (
                user_id INT AUTO_INCREMENT PRIMARY KEY,
                user_name VARCHAR(50) NOT NULL,
                user_email VARCHAR(100) NOT NULL UNIQUE,
                user_birthday DATE,
                user_registered_id INT,
                user_deleted_id INT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """)
        self.db_connection.commit()
        self._seed_users()
        cursor.close()

    def _seed_users(self):
        cursor = self.db_connection.cursor()
        cursor.executemany("""
            INSERT IGNORE INTO users_table (user_name, user_email, user_birthday, user_registered_id, user_deleted_id)
            VALUES (%s, %s, %s, %s, %s)
        """, [
            ("admin", "admin@gmail.com", "1981-11-13", 1, None),
            ("test", "test@gmail.com", "2004-11-30", 2, None),
            ("eli", "elisdev@gmail.com", "2005-07-30", 3, None),
        ])
        self.db_connection.commit()
        cursor.close()

    # ---------- ROLES ----------
    def _install_roles(self):
        cursor = self.db_connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS roles_table (
                role_id VARCHAR(16) PRIMARY KEY,
                role_description VARCHAR(512),
                role_can_create TINYINT NOT NULL DEFAULT 0,
                role_can_read TINYINT NOT NULL DEFAULT 0,
                role_can_update TINYINT NOT NULL DEFAULT 0,
                role_can_delete TINYINT NOT NULL DEFAULT 0
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """)
        self.db_connection.commit()
        self._seed_roles()
        cursor.close()

    def _seed_roles(self):
        cursor = self.db_connection.cursor()
        cursor.executemany("""
            INSERT IGNORE INTO roles_table (role_id, role_description, role_can_create, role_can_read, role_can_update, role_can_delete)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, [
            ("admin", "Администратор системы", 1, 1, 1, 1),
            ("editor", "Редактор контента", 0, 1, 1, 0),
            ("guest", "Гость с ограничениями", 0, 1, 0, 0),
            ("user", "Обычный пользователь", 0, 1, 0, 0),
        ])
        self.db_connection.commit()
        cursor.close()

    # ---------- USER ACCESS ----------
    def _install_user_access(self):
        cursor = self.db_connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS user_access_table (
                user_access_id CHAR(36) PRIMARY KEY,
                user_id CHAR(36),
                role_id VARCHAR(16),
                user_access_login VARCHAR(32),
                user_access_salt VARCHAR(16),
                user_access_dk VARCHAR(64)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """)
        self.db_connection.commit()
        self._seed_user_access()
        cursor.close()

    def _seed_user_access(self):
        cursor = self.db_connection.cursor()
        data = [
            ("A001", "1", "admin", "admin_login", "saltA"),
            ("A002", "2", "user", "test_login", "saltB"),
            ("A003", "3", "editor", "eli_login", "saltC"),
        ]
        insert_data = [(uid, uid_user, role, login, salt, self._kdf1(login, salt)) for uid, uid_user, role, login, salt in data]
        cursor.executemany("""
            INSERT IGNORE INTO user_access_table (user_access_id, user_id, role_id, user_access_login, user_access_salt, user_access_dk)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, insert_data)
        self.db_connection.commit()
        cursor.close()

    # ---------- TOKENS ----------
    def _install_tokens(self):
        cursor = self.db_connection.cursor()
        cursor.execute("""
            CREATE TABLE IF NOT EXISTS tokens_table (
                token_id CHAR(36) PRIMARY KEY,
                user_access_id CHAR(36),
                token_issued_at DATETIME,
                token_expired DATETIME,
                token_type VARCHAR(32)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
        """)
        self.db_connection.commit()
        self._seed_tokens()
        cursor.close()

    def _seed_tokens(self):
        cursor = self.db_connection.cursor()
        now = datetime.now()
        cursor.executemany("""
            INSERT IGNORE INTO tokens_table (token_id, user_access_id, token_issued_at, token_expired, token_type)
            VALUES (%s, %s, %s, %s, %s)
        """, [
            ("T001", "A001", now, now + timedelta(days=7), "access"),
            ("T002", "A002", now, now + timedelta(days=3), "refresh"),
            ("T003", "A003", now, now + timedelta(days=1), "access"),
        ])
        self.db_connection.commit()
        cursor.close()

    # ---------- HASH + KDF ----------
    def _hash(self, text: str) -> str:
        return hashlib.md5(text.encode("utf-8")).hexdigest()

    def _kdf1(self, password: str, salt: str) -> str:
        value = password + salt
        for _ in range(1000):
            value = self._hash(value)
        return value

    # ---------- DISPLAY ----------
    def show_users(self):
        cursor = self.db_connection.cursor(dictionary=True)
        cursor.execute("SELECT user_id, user_name, user_email, user_birthday FROM users_table")
        print("Пользователи")
        print("-" * 90)
        for r in cursor.fetchall():
            print(f"{r['user_id']:<4} | {r['user_name']:<10} | {r['user_email']:<25} | {r['user_birthday']}")
        print()
        cursor.close()

    def show_roles(self):
        cursor = self.db_connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM roles_table")
        print("Роли")
        print("-" * 90)
        for r in cursor.fetchall():
            print(f"{r['role_id']:<10} | {r['role_description']:<25} | "
                  f"C:{r['role_can_create']} R:{r['role_can_read']} U:{r['role_can_update']} D:{r['role_can_delete']}")
        print()
        cursor.close()

    def show_user_access(self):
        cursor = self.db_connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM user_access_table")
        print("Доступ пользователей")
        print("-" * 90)
        for r in cursor.fetchall():
            print(f"{r['user_access_id']:<8} | {r['user_id']:<8} | {r['role_id']:<8} | "
                  f"{r['user_access_login']:<15} | SALT:{r['user_access_salt']:<6} | DK:{r['user_access_dk']}")
        print()
        cursor.close()

    def show_tokens(self):
        cursor = self.db_connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM tokens_table")
        print("Токены")
        print("-" * 90)
        for r in cursor.fetchall():
            print(f"{r['token_id']:<6} | {r['user_access_id']:<8} | {r['token_issued_at']} | {r['token_expired']} | {r['token_type']}")
        print()
        cursor.close()

    def close(self):
        if self.db_connection.is_connected():
            self.db_connection.close()
            print("Соединение закрыто")


def main():
    try:
        db = DataAccess("db.json")
        db.install()
    except Exception as err:
        frame = inspect.currentframe()
        print(f"[{inspect.getframeinfo(frame).function}:{frame.f_lineno}] {type(err).__name__}: {err}")
    finally:
        if 'db' in locals():
            db.close()


if __name__ == "__main__":
    main()
