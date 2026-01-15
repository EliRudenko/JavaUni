# DAO/DAL - Data Access Object / Layer - архітектурний шар, що 
# інкапсулює роботу з даними, надаючи програмний інтерфейс
import hashlib 
import helper
import json
import mysql.connector
import sys

class DataAccessor :
    def __init__(self, ini_file="./db.json"):
        try :
            with open(ini_file, encoding="utf-8") as file :
                self.ini = json.load(file)
        except OSError as err :
            raise RuntimeError("Ini read error: " + str(err))
        
        try :
            self.db_connection = mysql.connector.connect( **self.ini )
        except mysql.connector.Error as err :
            raise RuntimeError("Connection error: " + str(err))


    def install(self) :
        try :
            self._install_users()
            self._install_roles()
            self._install_user_access()
            self._install_tokens()
        except Exception as err :
            print(err)


    def _install_users(self) :
        sql = '''CREATE TABLE  IF NOT EXISTS  users (
            user_id             CHAR(36)     NOT NULL PRIMARY KEY DEFAULT( UUID() ),
            user_name           VARCHAR(64)  NOT NULL,
            user_email          VARCHAR(128) NOT NULL,
            user_birthdate      DATETIME         NULL,
            user_registered_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
            user_deleted_at     DATETIME         NULL
        ) ENGINE = InnoDb   DEFAULT CHARSET = utf8mb4   COLLATE = utf8mb4_unicode_ci
        '''
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in _install_users")
        with self.db_connection.cursor() as cursor :
            cursor.execute(sql)


    def _install_roles(self) :
        sql = '''CREATE TABLE  IF NOT EXISTS  roles (
            role_id             VARCHAR(16)  NOT NULL PRIMARY KEY,
            role_description    VARCHAR(512) NOT NULL,
            role_can_create     TINYINT      NOT NULL DEFAULT 0,
            role_can_read       TINYINT      NOT NULL DEFAULT 0,
            role_can_update     TINYINT      NOT NULL DEFAULT 0,
            role_can_delete     TINYINT      NOT NULL DEFAULT 0
        ) ENGINE = InnoDb   DEFAULT CHARSET = utf8mb4   COLLATE = utf8mb4_unicode_ci
        '''
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in _install_roles")
        with self.db_connection.cursor() as cursor :
            cursor.execute(sql)


    def _install_user_access(self) :
        sql = '''CREATE TABLE  IF NOT EXISTS  user_accesses (
            user_access_id      CHAR(36)     NOT NULL PRIMARY KEY DEFAULT( UUID() ),
            user_id             CHAR(36)     NOT NULL,
            role_id             VARCHAR(16)  NOT NULL,
            user_access_login   VARCHAR(32)  NOT NULL,
            user_access_salt    CHAR(16)     NOT NULL,
            user_access_dk      CHAR(20)     NOT NULL COMMENT 'Derived Key by RFC 2898',
            UNIQUE(user_access_login)
        ) ENGINE = InnoDb   DEFAULT CHARSET = utf8mb4   COLLATE = utf8mb4_unicode_ci
        '''
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor() as cursor :
            cursor.execute(sql)
    

    def _install_tokens(self) :
        sql = '''CREATE TABLE  IF NOT EXISTS  tokens (
            token_id         CHAR(36)     NOT NULL PRIMARY KEY DEFAULT( UUID() ),
            user_access_id   CHAR(36)     NOT NULL,
            token_issued_at  DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP,
            token_expired_at DATETIME     NOT NULL,
            token_type       VARCHAR(16)      NULL
        ) ENGINE = InnoDb   DEFAULT CHARSET = utf8mb4   COLLATE = utf8mb4_unicode_ci
        '''
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor() as cursor :
            cursor.execute(sql)
            

    def _hash(self, input:str) -> str :
        hash = hashlib.sha3_256()  
        hash.update(input.encode(encoding='utf-8'))
        return hash.hexdigest()


    def _kdf1(self, password:str, salt:str) -> str :
        iteration_count = 1000    # RFC 2898 recommendation
        dk_len = 20
        t = self._hash(password + salt)
        for i in range(iteration_count) :
            t = self._hash(t)
        return t[:dk_len]    


    def kdf(self, password:str, salt:str) -> str :
        return self._kdf1(password, salt)


    def get_db_identity(self):
        '''Генерація ID за правилами обліку в БД'''
        sql = "select uuid()"
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor() as cursor :
            cursor.execute(sql)
            return next(cursor)[0]
        

    def _seed_roles(self) :
        '''Сідування (seed - зерно) - утворення початкових значень в алгоритмі чи БД
        Для БД користувачів необхідно щонайменше 2 ролі - максимальна (admin) та мінімальна (guest)
        а також користувач (user + access) з максимальною роллю'''
        sql = '''INSERT INTO roles(role_id, role_description, role_can_create,
        role_can_read, role_can_update, role_can_delete) VALUES(?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE 
        role_description = VALUES(role_description),
        role_can_create = VALUES(role_can_create),
        role_can_read = VALUES(role_can_read),
        role_can_update = VALUES(role_can_update),
        role_can_delete = VALUES(role_can_delete) '''
        roles = [
            ['admin', 'Root administrator', 1, 1, 1, 1],
            ['user', 'Self registered user', 0, 0, 0, 0],
        ]
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor(prepared=True) as cursor :
            cursor.executemany(sql, roles)  # !! У деяких драйверах відсутнє автоматичне
            #cursor.execute(sql2)           # !! закриття транзакцій, через це дані не 
            self.db_connection.commit()     # !! потрапляють до БД без команди commit


    def _seed_users(self) :
        id = '296c7f07-ba1a-11f0-83b6-62517600596c'
        sql = """INSERT INTO users(user_id, user_name, user_email) VALUES(?, ?, ?)
        ON DUPLICATE KEY UPDATE
        user_name = VALUES(user_name), user_email = VALUES(user_email)"""
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor(prepared=True) as cursor :
            cursor.execute(sql, (id, 'Default Administrator', 'change.me@fake.net',))
            self.db_connection.commit()

        access_id = '82616f45-ba1d-11f0-83b6-62517600596c'    
        salt = helper.generate_salt(16)
        login = 'admin'
        password = 'admin'
        dk = self.kdf(password, salt)
        sql = """INSERT INTO user_accesses(user_access_id, user_id, role_id, user_access_login,
        user_access_salt, user_access_dk) VALUES(?, ?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
        user_id = VALUES(user_id), 
        role_id = VALUES(role_id), 
        user_access_login = VALUES(user_access_login), 
        user_access_salt = VALUES(user_access_salt), 
        user_access_dk = VALUES(user_access_dk)"""
        with self.db_connection.cursor(prepared=True) as cursor :
            cursor.execute(sql, (access_id, id, 'admin', login, salt, dk))
            self.db_connection.commit()


    def seed(self) :
        self._seed_roles()
        self._seed_users()


    def authenticate(self, login:str, password:str) -> dict|None :
        sql = 'SELECT * FROM users u JOIN user_accesses ua ON u.user_id = ua.user_id WHERE ua.user_access_login = ?'
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor(prepared=True, dictionary=True) as cursor :
            cursor.execute(sql, (login,))
            row = next(cursor, None)
        if row is None :
            return None 
        # оскільки DK незворотній, для перевірки ми генеруємо DK на базі солі,
        # що зберігається у БД та паролю, що приходить параметром, і перевіряємо
        # рівність тому DK, що зберігається у БД
        dk = self.kdf(password, row["user_access_salt"])
        return row if dk == row["user_access_dk"] else None
    

    def register_user(self, name:str, email:str, login:str, password:str, birthdate:str|None=None) :
        sql = "SELECT COUNT(*) FROM user_accesses WHERE user_access_login = ?"
        if self.db_connection is None : 
            raise RuntimeError("Connection empty in " + sys._getframe(0).f_code.co_name)
        with self.db_connection.cursor(prepared=True) as cursor :
            cursor.execute(sql, (login, ))
            cnt = next(cursor)[0]
        if cnt > 0 :
            raise ValueError("Login in use")
        user_id = self.get_db_identity()
        salt = helper.generate_salt()
        dk = self.kdf(password, salt)
        try :
            with self.db_connection.cursor(prepared=True) as cursor :
                cursor.execute(
                    "INSERT INTO users(user_id, user_name, user_email) VALUES(?, ?, ?)", 
                    (user_id, name, email))
                cursor.execute(
                    "INSERT INTO user_accesses(user_access_id, user_id, role_id, user_access_login, user_access_salt, user_access_dk) VALUES( UUID(), ?, 'user', ?, ?, ?)", 
                    (user_id, login, salt, dk))
                self.db_connection.commit()                
        except mysql.connector.Error as err :
            print(err)
            self.db_connection.rollback()
            raise RuntimeError(str(err))
        else :
            return user_id




def main() :
    try :
        data_accessor = DataAccessor()
        
        # 1. СПОЧАТКУ створюємо таблиці (розкоментуйте цей рядок)
        data_accessor.install() 
        
        # 2. ПОТІМ наповнюємо їх даними
        data_accessor.seed()
        
        # print(data_accessor.get_db_identity())
    except RuntimeError as err :
        print(err)
        return
    else :
        # ... решта коду ...
        print(helper.generate_salt())

    login = input("login: ")
    password = input("password: ")
    print(data_accessor.authenticate(login, password))

if __name__ == '__main__' :
    main()
