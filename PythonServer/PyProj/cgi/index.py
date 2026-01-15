#!C:/Users/morri/AppData/Local/Programs/Python/Python313/python.exe

import os
import sys

sys.stdout.reconfigure(encoding='utf-8')

sorted_envs = sorted(os.environ.items(), key=lambda item: item[0])

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

print("Content-Type: text/html; charset=utf-8\n")
print("Content-Length:", len(html.encode('utf-8')))
print()
print(html)
