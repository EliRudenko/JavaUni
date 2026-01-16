import sys
from models.request import CgiRequest

class OrdertestController:

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

        with open('./views/ordertest_index.html', 'rt', encoding="utf-8") as f:
            body = f.read()

        print(layout.replace("<!-- RenderBody -->", body))
