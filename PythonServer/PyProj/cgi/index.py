# Шебанг вказує інтерпретатор Python для CGI.
#!C:/Users/morri/AppData/Local/Programs/Python/Python313/python.exe

# os потрібен для доступу до змінних оточення (CGI-параметри).
import os
# sys потрібен для налаштування stdout та виводу байтів.
import sys

# Перемикаємо кодування stdout, щоб коректно віддавати UTF-8.
sys.stdout.reconfigure(encoding='utf-8')

# CGI передає параметри запиту через змінні оточення.
sorted_envs = sorted(os.environ.items(), key=lambda item: item[0])

# Формуємо HTML-таблицю зі змінних оточення.
table_rows = "".join(f"<tr><td>{k}</td><td>{v}</td></tr>\n" for k, v in sorted_envs)
envs_table = f"""
<table border="1" cellpadding="5" cellspacing="0">
    <thead>
        <tr>
            <th>Параметр</th>
            <th>Значення</th>
        </tr>
    </thead>
    <tbody>
        {table_rows}
    </tbody>
</table>
"""

# Повний HTML-документ як рядок.
html = f'''
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Python-CGI</title>
    <link rel="icon" href="/python.png" />
</head>
<body>
<h1>Змінні оточення</h1>
<p>Згідно з принципами CGI всі параметри від сервера до скрипту передаються як змінні оточення</p>
{envs_table}
</body>
</html>
'''

# CGI-відповідь складається з HTTP-заголовків + порожній рядок + тіло.
print("Content-Type: text/html; charset=utf-8\n")  # Заголовок типу контенту.
print("Content-Length:", len(html.encode('utf-8')))  # Довжина відповіді.
print()  # Порожній рядок відділяє заголовки від тіла.
print(html)  # Тіло відповіді.
