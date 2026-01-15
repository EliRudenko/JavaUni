from models.request import CgiRequest
import sys, json, io

class HomeController :

    def __init__(self, request:CgiRequest):
        self.request = request

    def privacy(self):
        html = """<!DOCTYPE html>
    <html lang="uk">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Політика конфіденційності</title>
        <link rel="icon" href="/img/python.png" />
        <link rel="stylesheet" href="/css/site.css" />
    </head>
    <body>
    <h1>Політика конфіденційності</h1>
    <p>
        Згідно з принципів CGI всі параметри від сервера (Apache) до скрипту
        передаються як змінні оточення.
    </p>
    </body>
    </html>
    """
        print("Content-Type: text/html; charset=utf-8")
        print()
        print(html)


    def serve(self):
        # определяем действие
        action = self.request.path_parts[1].lower() if len(self.request.path_parts) > 1 else "index"

        # ищем метод с таким именем
        controller_action = getattr(self, action, None)
        if controller_action:
            controller_action()
        else:
            print("Status: 404 Not Found")
            print("Content-Type: text/html; charset=utf-8")
            print()
            print(f"<h1>Action '{action}' not found in HomeController</h1>")


    def params(self):
        envs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.server.items()) + "</ul>\n"
        hdrs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.headers.items()) + "</ul>\n"
        qp = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.query_params.items()) + "</ul>\n"

        html = f"""<!DOCTYPE html>
        <html lang="uk">
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Параметри CGI</title>
            <link rel="icon" href="/img/python.png" />
            <link rel="stylesheet" href="/css/site.css" />
        </head>
        <body>
        <h1>Усі параметри, передані диспетчером доступу</h1>

        <h2>Змінні оточення</h2>
        {envs}

        <h2>Заголовки</h2>
        {hdrs}

        <h2>Query-параметри</h2>
        {qp}

        <p><a href="/home/index">Назад</a></p>
        </body>
        </html>
        """

        print("Content-Type: text/html; charset=utf-8")
        print()
        print(html)


    def index(self):
        html = """<!DOCTYPE html>
        <html lang="uk">
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>AM-CGI</title>
            <link rel="icon" href="/img/python.png" />
            <link rel="stylesheet" href="/css/site.css" />
        </head>
        <body>
        <h1>Головна</h1>

        <ul>
            <li><a href="/home/params">Усі параметри (CGI)</a></li>
            <li><a href="/home/privacy">Політика конфіденційності</a></li>
        </ul>

        </body>
        </html>
        """

        print("Content-Type: text/html; charset=utf-8")
        print()
        print(html)



    # def index(self) :        
    #     envs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.server.items()) + "</ul>\n"
    #     hdrs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.headers.items()) + "</ul>\n"
    #     qp = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k,v in self.request.query_params.items()) + "</ul>\n"

    #     html = f"""<!DOCTYPE html>
    #     <html lang="en">
    #     <head>
    #         <meta charset="utf-8">
    #         <meta name="viewport" content="width=device-width, initial-scale=1.0">
    #         <title>AM-CGI</title>
    #         <link rel="icon" href="/img/python.png" />
    #         <link rel="stylesheet" href="/css/site.css" />
    #     </head>
    #     <body>
    #     <h1>Змінні оточення (диспетчер доступу)</h1>
    #     <p>
    #         Згідно з принципів CGI всі параметри від сервера (Apache) до скрипту
    #         передаються як змінні оточення. <a href="/home/privacy">Політика конфіденційності</a>
    #     <p>
    #     {envs}
    #     {hdrs}
    #     {qp}
    #     <img src="/img/Python.png" width="100" />
    #     <img src="/img/person.jpg" width="100" />
    #     <script src="/js/site.js"></script>
    #     </body>
    #     </html>
    #     """

    #     print("Content-Type: text/html; charset=utf-8")
    #     # print(f"Content-Length: {len(html)}")
    #     print()
    #     print(html)
