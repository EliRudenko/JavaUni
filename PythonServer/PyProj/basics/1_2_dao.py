####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 19: Робота з базами даних: порядок дій для підключення
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 20: Робота з БД: виконання SQL-запитів, параметри, результати
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 19: DAO ОПИСЫВАЕТ ПОДКЛЮЧЕНИЕ И РАБОТУ С БД КАК ОТДЕЛЬНЫЙ СЛОЙ.
# - ВОПРОС 20: МЕТОДЫ DAO ВЫПОЛНЯЮТ SQL-ЗАПРОСЫ И ВОЗВРАЩАЮТ РЕЗУЛЬТАТЫ.
#


# ДЗ 1 PBKDF2 (RFC2898) + ДЗ 2: Реєстрація з датою народження
# БД и хеширование паролей

# Імпорти стандартних/зовнішніх модулів для роботи з БД і криптографією.
from datetime import datetime          # робота з датами (дата народження, перевірка "не з майбутнього")
import hmac                            # HMAC потрібен для PBKDF2 (RFC2898)
import json                            # читаємо налаштування БД з JSON
from math import ceil                  # округлення вгору для розрахунку блоків PBKDF2
import mysql.connector                 # драйвер MySQL
import sys                             # системні дані, корисно для помилок
import hashlib                         # базові хеш-функції
import helper                          # допоміжні функції (наприклад, генерація солі)

class DataAccessor:
    def __init__(self, ini_file: str = './db.json'):
        # Читаємо конфігурацію підключення до БД з JSON-файла.
        try:
            with open(ini_file, 'r', encoding='utf-8') as f:
                self.ini = json.load(f)          # словник з параметрами підключення.
        except OSError as err:
            # Якщо конфіг відсутній — робота неможлива.
            raise RuntimeError("Неможливо продовжити без конфігурації бази даних.")
        try:
            # Підключаємося до MySQL з параметрами з конфігурації.
            self.db_connection = mysql.connector.connect(**self.ini)
        except mysql.connector.Error as err:
            # Якщо з'єднання не вдалось, повідомляємо помилку.
            raise RuntimeError("Неможливо продовжити без підключення до бази даних.")
    
    def install(self):
        # Создание всех необходимых таблиц
        try:
            self._install_users()       # таблица users
            self._install_roles()       # таблица roles
            self._install_accesses()    # таблица accesses
            self._install_tokens()      # таблица tokens
        except Exception as err:
            print(err)

    def _install_users(self):
        # Таблица users для хранения информации о пользователях
        # Поле user_datebirth  NULL дата рождения необязательна
        #  для ДЗ №2,  дата рождения может отсутствовать
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
            # Защита когда подключения нет
            raise RuntimeError("Немає підключення до бази даних.(install_users)")
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)         # выполняем SQL
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")  # выводим ошибку
            else:
                print("Таблиця users успішно створена або вже існує.")
    
    def _install_roles(self):
        # Таблица ролей, которые определяют права доступа
        # роли: admin, user
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
        # Таблица доступов: логин, соль и derived key
        # user_access_dk — результат PBKDF2 (RFC2898) / KDF1
        # хранится "производный ключ" не пароль в чистом виде
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
        # Таблица токенов JWT/сессии
        #  для хранения выданных токенов и сроков действия
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
        # SHA3-256 для KDF1Ю, демонстрация
        # KDF1  упрощённый алгоритм многоразового хеширования
        hash = hashlib.sha3_256()              # создаём объект SHA3-256
        input = hash.update(input.encode('utf-8'))  # обновляем данные
        return hash.hexdigest()                # возвращаем hex-строку
    
    def kdf1(self, password: str, salt: str) -> str:
        # KDF1 итеративное хеширование
        # соединяем пароль и соль, затем многократно хешируем
        iterations = 1000               # количество повторов
        dk_len = 20                     # длина производного ключа
        t = self._hash(password + salt) # первичный хеш
        for _ in range(iterations):
            t = self._hash(t)           # повторяем хеширование
        return t[:dk_len]               # возвращаем укороченный DK
    
    def _int_to_4be(self, i: int) -> bytes:
        # Преобразование int в 4байтовый big-endian для PBKDF2
        # Это нужно для индекса блока в PBKDF2
        return i.to_bytes(4, byteorder='big')
    
    def pbkdf2_hmac_custom(self, password: str, salt: str,
                           hash_name: str = 'sha256') -> str:
        # Реализация PBKDF2 /RFC2898 без встроенных функций
        # Используется HMAC и XOR всех блоков Ui
        iterations = 1000                      # число итераций
        dklen = 20                             # длина результата
        password_bytes = password.encode('utf-8')  # пароль в bytes
        salt_bytes = salt.encode('utf-8')          # соль в bytes
        hlen = hashlib.new(hash_name).digest_size  # длина хеша

        if dklen > (2 ** 32 - 1) * hlen:
            # Ограничение из RFC слишком длинный ключ нельзя получить
            raise ValueError("Derived key too long")

        l = ceil(dklen / hlen)   # количество блоков
        dk = b''                 # итоговый ключ (bytes)

        for block_index in range(1, l + 1):
            # U1 = HMAC(password, salt + INT(block_index))
            u = hmac.new(
                password_bytes,
                salt_bytes + self._int_to_4be(block_index),
                getattr(hashlib, hash_name)
            ).digest()
            t = bytearray(u)  # T = U1
            for _ in range(1, iterations):
                # U(i) = HMAC(password, U(i-1))
                u = hmac.new(password_bytes, u, getattr(hashlib, hash_name)).digest()
                # XOR накопление блока T
                for i in range(len(u)):
                    t[i] ^= u[i]
            dk += bytes(t)  # добавляем блок T к итоговому ключу

        # Приводим результат к hex-строке
        return dk[:dklen // 2].hex()
    
    def _seed_roles(self):
        # Начальные роли системы
        # В проекте достаточно двух ролей: admin и user
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
            ('admin', 'Root administrator', 1, 1, 1, 1),  # администратор
            ('user', 'Regular user', 0, 0, 0, 0)          # обычный пользователь
        ]

        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.executemany(sql, roles)    # множественная вставка
                self.db_connection.commit()       # сохраняем изменения
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
            else:
                print("Таблиця tokens успішно створена або вже існує.")

    def get_id_identity(self) -> str:
        # UUID из БД удобно для консистентности
        # Это гарантирует одинаковый формат UUID везде
        sql = "SELECT uuid()"
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor() as cursor:
            try:
                cursor.execute(sql)       # выполняем запрос
                return next(cursor)[0]    # получаем UUID
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")

    def _seed_users(self):
        # Начальный администратор
        # логин/пароль: admin/admin
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
        salt = helper.generate_salt()            # генерируем соль
        access_id = "71240707-bf53-11f0-95f7-0250f2882c00"
        login = 'admin'
        password = 'admin'
        dk = self.kdf1(password, salt)           # derived key от пароля и соли
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
        # Заполнение таблиц начальными данными
        # стартовые данные
        try:
            self._seed_roles()   # создаем роли
            self._seed_users()   # создаем пользователя admin
        except Exception as err:
            print(err)

    def authenticate(self, login: str, password: str) -> dict | None:
        # Перевірка користувача за логіном і derived key (DK).
        # SQL виконує JOIN між users і accesses.
        sql = '''SELECT *
                FROM users u
                JOIN accesses a ON a.user_id = u.user_id
                WHERE a.user_access_login = %s;
        '''
        if self.db_connection is None:
            raise RuntimeError("Немає підключення до бази даних" + sys._getframe().f_code.co_name)
        with self.db_connection.cursor(dictionary=True) as cursor:
            try:
                cursor.execute(sql, (login,))           # Параметризований запит.
                row = next(cursor, None)                # Перша знайдена запис.
                if row is None:
                    return None                         # Користувача не знайдено.
                dk = self.kdf1(password, row['user_access_salt'])  # DK із пароля + солі.
                return row if dk == row['user_access_dk'] else None
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
                return None
    
    def register_user(self, name: str, email: str,
                      login: str, password: str, birthdate: str|None = None) -> bool:
        # Регистрация с опциональной датой рождения ДЗ №2!!!!
        # валидируется дату, проверяется логин, пишется в БД
        birthdate_value = None
        if birthdate and birthdate.strip():
            try:
                parsed = datetime.strptime(birthdate, "%Y-%m-%d")  # формат YYYY-MM-DD
                # Запрет будущие даты
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
                cursor.execute(sql, (login,))          # проверяем логин
                count = next(cursor)[0]
                if count > 0:
                    raise ValueError("Користувач з таким логіном вже існує.")
                user_id = self.get_id_identity()       # генерируем UUID
                salt = helper.generate_salt()          # генерируем соль
                dk = self.kdf1(password, salt)         # производный ключ
                with self.db_connection.cursor() as cursor2:
                    cursor.execute(
                        "INSERT INTO users (user_id, user_name, user_email, user_datebirth) VALUES (%s, %s, %s, %s)",
                        (user_id, name,  email, birthdate_value,)
                    )
                    cursor2.execute(
                        '''INSERT INTO accesses 
                                    (access_id, user_id, role_id, user_access_login, user_access_salt, user_access_dk)
                                    VALUES (UUID(), %s, 'user', %s, %s, %s)''',
                        (user_id, login, salt, dk,)
                    )
                self.db_connection.commit()            # сохраняем изменения
            except mysql.connector.Error as err:
                print(f"Помилка виконання запиту: {err}")
                self.db_connection.rollback()
                return False
            else:
                return user_id
        


def main():
    # CLI-скрипт для ручного тестирования
    # создает таблицы, позволяет создать пользователя
    try:
        dataAccessor = DataAccessor()     # создаем объект доступа к БД
        dataAccessor.install()            # создаем таблицы
        print(dataAccessor.get_id_identity())
        dataAccessor.seed()               # заполняем стартовыми данными
    except RuntimeError:
        print("Error")
    else:
        # print("kdf1", dataAccessor.kdf1("123", "456")) #b21dd5db016c2c7052c3
        # print("pbkdf2", dataAccessor.pbkdf2_hmac_custom("123", "456")) #a51a8bc1342be8360015
        print("Підключення до бази даних успішне.")

    name = input("Name: ")                     # имя
    email = input("Email: ")                   # email
    login = input("Login: ")                   # логин
    password = input("Password: ")             # пароль
    birthdate = input("Birthdate (YYYY-MM-DD) or empty: ")  # дата рождения
    try:
        print(dataAccessor.register_user(name, email, login, password, birthdate))
    except Exception as err:
        print("Error register user:", err)

if __name__ == "__main__":
    main()
