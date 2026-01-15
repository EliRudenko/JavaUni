from datetime import datetime
import hmac
import json
from math import ceil
import mysql.connector
import sys
import hashlib
import helper

class DataAccessor:
    def __init__(self, ini_file: str = './db.json'):
        try:
            with open(ini_file, 'r', encoding='utf-8') as f:
                self.ini = json.load(f)
        except OSError as err:
            raise RuntimeError("Неможливо продовжити без конфігурації бази даних.")
        try:
            self.db_connection = mysql.connector.connect(**self.ini)
        except mysql.connector.Error as err:
            raise RuntimeError("Неможливо продовжити без підключення до бази даних.")
    
    def install(self):
        try:
            self._install_users()
            self._install_roles()
            self._install_accesses()
            self._install_tokens()
        except Exception as err:
            print(err)

    def _install_users(self):
        sql = '''
        CREATE TABLE IF NOT EXISTS users (
            user_id            CHAR(36)     NOT NULL PRIMARY KEY DEFAULT (UUID()),
            user_name          VARCHAR(64)  NOT NULL,
            user_email         VARCHAR(128) NOT NULL,
            user_datebirth     DATETIME     NULL,
            user_registered_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
            user_deleted_at    DATETIME     NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних.(install_users)")
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця users успішно створена або вже існує.")
    
    def _install_roles(self):
        sql = '''
        CREATE TABLE IF NOT EXISTS roles (
            role_id          VARCHAR(16)  NOT NULL PRIMARY KEY,
            role_description VARCHAR(512)  NOT NULL,
            role_can_create  TINYINT NOT NULL DEFAULT 0,
            role_can_read    TINYINT NOT NULL DEFAULT 0,
            role_can_update  TINYINT NOT NULL DEFAULT 0,
            role_can_delete  TINYINT NOT NULL DEFAULT 0
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних. (install_roles)")
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця user_roles успішно створена або вже існує.")

    def _install_accesses(self):
        sql = '''
        CREATE TABLE IF NOT EXISTS accesses (
            access_id    CHAR(36)  NOT NULL PRIMARY KEY DEFAULT (UUID()),
            user_id           CHAR(36)  NOT NULL,
            role_id           VARCHAR(16) NOT NULL,
            user_access_login VARCHAR(32) NOT NULL,
            user_access_salt  CHAR(16)  NOT NULL,
            user_access_dk  CHAR(20)  NOT NULL COMMENT 'Derived Key by RFC2898',
            UNIQUE(user_access_login)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця user_accesses успішно створена або вже існує.")

    def _install_tokens(self):
        sql = '''
        CREATE TABLE IF NOT EXISTS tokens (
            token_id    CHAR(36)  NOT NULL PRIMARY KEY DEFAULT (UUID()),
            user_access_id   CHAR(36)  NOT NULL,
            issued_at        DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
            expires_at       DATETIME  NOT NULL,
            token_type       VARCHAR(16) NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця tokens успішно створена або вже існує.")
    
    def _hash(self, input: str,) -> str:
        hash = hashlib.sha3_256()
        input = hash.update(input.encode('utf-8'))
        return hash.hexdigest()
    
    def kdf1(self, password: str, salt: str) -> str:
        iterations = 1000
        dk_len = 20
        t = self._hash(password + salt)
        for _ in range(iterations):
            t = self._hash(t)
        return t[:dk_len]
    
    def _int_to_4be(self, i: int) -> bytes:
        return i.to_bytes(4, byteorder='big')
    
    def pbkdf2_hmac_custom(self, password: str, salt: str,
                           hash_name: str = 'sha256') -> str:
        iterations = 1000
        dklen = 20
        password_bytes = password.encode('utf-8')
        salt_bytes = salt.encode('utf-8')
        hlen = hashlib.new(hash_name).digest_size

        if dklen > (2 ** 32 - 1) * hlen:
            raise ValueError("Derived key too long")

        l = ceil(dklen / hlen)
        dk = b''

        for block_index in range(1, l + 1):
            u = hmac.new(password_bytes,
                         salt_bytes + self._int_to_4be(block_index),
                         getattr(hashlib, hash_name)).digest()
            t = bytearray(u)
            for _ in range(1, iterations):
                u = hmac.new(password_bytes, u,
                             getattr(hashlib, hash_name)).digest()
                for i in range(len(u)):
                    t[i] ^= u[i]
            dk += bytes(t)

        return dk[:dklen // 2].hex()
    
    def _seed_roles(self):
        sql = '''
        INSERT INTO roles (
            role_id,
            role_description,
            role_can_create,
            role_can_read,
            role_can_update,
            role_can_delete
        ) VALUES (%s, %s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            role_description = VALUES(role_description),
            role_can_create = VALUES(role_can_create),
            role_can_read = VALUES(role_can_read),
            role_can_update = VALUES(role_can_update),
            role_can_delete = VALUES(role_can_delete)
        '''
        roles = [
            ('admin', 'Root administrator', 1, 1, 1, 1),
            ('user', 'Regular user', 0, 0, 0, 0)
        ]

        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.executemany(sql, roles)
                self.db_connection.commit()
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця tokens успішно створена або вже існує.")

    def get_id_identity(self) -> str:
        sql = "SELECT uuid()"
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)
                return next(cursor)[0]
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")

    def _seed_users(self):
        id = "f7335c2f-bf51-11f0-95f7-0250f2882c00"
        sql = '''
        INSERT INTO users (
            user_id,
            user_name,
            user_email
        ) VALUES (%s, %s, %s)
        ON DUPLICATE KEY UPDATE
            user_email = VALUES(user_email),
            user_datebirth = VALUES(user_datebirth)
        '''
        users = [
            (id, 'Default administrator', 'change.me@fake.net')
        ]
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.executemany(sql, users)
                self.db_connection.commit()
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця users успішно створена або вже існує.")
        salt = helper.generate_salt()
        access_id = "71240707-bf53-11f0-95f7-0250f2882c00"
        login = 'admin'
        password = 'admin'
        dk = self.kdf1(password, salt)
        sql = '''
        INSERT INTO accesses (
            access_id,
            user_id,
            role_id,
            user_access_login,
            user_access_salt,
            user_access_dk
        ) VALUES (%s, %s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            access_id = VALUES(access_id),
            user_id = VALUES(user_id),
            role_id = VALUES(role_id),
            user_access_login = VALUES(user_access_login),
            user_access_salt = VALUES(user_access_salt),
            user_access_dk = VALUES(user_access_dk)
        '''
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql, (access_id, id, 'admin', login, salt, dk))
                self.db_connection.commit()
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця accesses успішно створена або вже існує.")
    
    def seed(self):
        try:
            self._seed_roles()
            self._seed_users()
        except Exception as err:
            print(err)

    def authenticate(self, login: str, password: str) -> dict | None:
        sql = '''SELECT *
                FROM users u
                JOIN accesses a ON a.user_id = u.user_id
                WHERE a.user_access_login = %s;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor(dictionary=True) as cursor:
            try:
                cursor.execute(sql, (login,))
                row = next(cursor, None)
                if row is None:
                    return None
                dk = self.kdf1(password, row['user_access_salt'])
                return row if dk == row['user_access_dk'] else None
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
                return None
    
    def register_user(self, name: str, email: str,
                      login: str, password: str, birthdate: str|None = None) -> bool:
        birthdate_value = None
        if birthdate and birthdate.strip():
            try:
                parsed = datetime.strptime(birthdate, "%Y-%m-%d")
                if parsed > datetime.now():
                    raise ValueError("Дата народження не може бути з майбутнього")
                birthdate_value = parsed.strftime("%Y-%m-%d")
            except ValueError:
                raise ValueError("Невірний формат дати. Використовуйте YYYY-MM-DD")
        
        sql = "SELECT COUNT(*) FROM accesses WHERE user_access_login = %s"
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql, (login,))
                count = next(cursor)[0]
                if count > 0:
                    raise ValueError("Користувач з таким логіном вже існує.")
                user_id = self.get_id_identity()
                salt = helper.generate_salt()
                dk = self.kdf1(password, salt)
                with self.db_connection.cursor() as cursor2:
                    cursor.execute("INSERT INTO users (user_id, user_name, user_email, user_datebirth) VALUES (%s, %s, %s, %s)",
                                   (user_id, name,  email, birthdate_value,))
                    cursor2.execute('''INSERT INTO accesses 
                                    (access_id, user_id, role_id, user_access_login, user_access_salt, user_access_dk)
                                    VALUES (UUID(), %s, 'user', %s, %s, %s)''',
                                    (user_id, login, salt, dk,))
                self.db_connection.commit()
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
                self.db_connection.rollback()
                return False
            else:
                return user_id
        


def main():
    try:
        dataAccessor = DataAccessor()
        dataAccessor.install()
        print(dataAccessor.get_id_identity())
        dataAccessor.seed()
    except RuntimeError:
        print("Error")
    else:
        # print("kdf1", dataAccessor.kdf1("123", "456")) #b21dd5db016c2c7052c3
        # print("pbkdf2", dataAccessor.pbkdf2_hmac_custom("123", "456")) #a51a8bc1342be8360015
        print("Підключення до бази даних успішне.")

    name = input("Name: ")
    email = input("Email: ")
    login = input("Login: ")
    password = input("Password: ")
    birthdate = input("Birthdate (YYYY-MM-DD) or empty: ")
    try:
        print(dataAccessor.register_user(name, email, login, password, birthdate))
    except Exception as err:
        print("Error register user:", err)

if __name__ == "__main__":
    main()