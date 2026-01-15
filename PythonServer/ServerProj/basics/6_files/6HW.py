import mysql.connector
import os
from parser_ini import parse_ini

config_path = os.path.join(os.path.dirname(__file__), "db_config.ini")
db_ini = parse_ini(config_path)

if "pass" in db_ini:
    db_ini["password"] = db_ini.pop("pass")

db_connection = None

def connect_db():
    global db_connection
    try:
        db_connection = mysql.connector.connect(
            host=db_ini.get('host'),
            port=int(db_ini.get('port', 3308)),
            user=db_ini.get('user'),
            password=db_ini.get('password'),
            database=db_ini.get('database'),
            charset='utf8mb4',
            use_unicode=True
        )
    except mysql.connector.Error as err:
        print(err)
    else:
        print("Connection OK")

def close_connection():
    if db_connection:
        db_connection.close()

def show_uuid():
    sql = """select uuid(), uuid() 
    union all 
    select uuid(), uuid() 
    union all 
    select uuid(), uuid()"""
    global db_connection
    if db_connection is None: return
    try:
        cursor = db_connection.cursor()
        cursor.execute(sql)
        rows = cursor.fetchall()
        cols = [desc[0] for desc in cursor.description]
        print(f"{cols[0]:<40} | {cols[1]:<40}")
        print("-"*83)
        for row in rows:
            print(f"{row[0]:<40} | {row[1]:<40}")
    except mysql.connector.Error as err:
        print(err)
        print(sql)
    finally:
        cursor.close()

def main():
    connect_db()
    show_uuid()
    close_connection()

if __name__ == '__main__':
    main()
