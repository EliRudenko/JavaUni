#!C:/Python312/python.exe

import os

envs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in os.environ.items()) + "</ul>"

html = f"""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Python CGI</title>
    <link rel="icon" type="image/png" href="Python.png">
</head>
<body>
    <h1>Environment Variables</h1>
    <p>According to the CGI specification, all server (Apache) parameters are passed
    to this script as environment variables.</p>
    {envs}
</body>
</html>
"""

print("Content-Type: text/html; charset=utf-8\n")
print(html)



'''
CGI-script має починатися з коментаря, у якому після знака ! 
зазначається спосіб запуску даного файлу.
формування HTTP відповіді повністю покладається на скрипт, тому
передача заголовків також має туим частиною скрипту
'''