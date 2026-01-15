import sys
from models.request import CgiRequest

class HomeController:

    def __init__(self, request: CgiRequest):
        self.request = request

    def serve(self):
        action = (
            self.request.path_parts[1].lower()
            if len(self.request.path_parts) > 1 and len(self.request.path_parts[1].strip()) > 0
            else 'index'
        )
        controller_action = getattr(self, action)
        controller_action()

    def index(self):
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")
        
        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        with open('./views/home_index.html', 'rt', encoding="utf-8") as f:
            body = f.read()

        print(layout.replace("<!-- RenderBody -->", body))

    def privacy(self):
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")

        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        with open('./views/home_privacy.html', 'rt', encoding="utf-8") as f:
            body = f.read()

        print(layout.replace("<!-- RenderBody -->", body))

    def params(self):
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")

        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        envs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.server.items()) + "</ul>\n"
        hdrs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.headers.items()) + "</ul>\n"
        qp = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.query_params.items()) + "</ul>\n"

        with open('./views/home_params.html', 'rt', encoding="utf-8") as f:
            template = f.read()

        body = template.format(envs=envs, hdrs=hdrs, qp=qp)

        print(layout.replace("<!-- RenderBody -->", body))

