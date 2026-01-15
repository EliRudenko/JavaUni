class CgiRequest:
    # ДЗ 3 — объект запроса, который хранит результат анализа маршрута.
    # Этот класс — "контейнер" для всех данных запроса.
    def __init__(
        self,
        server: dict,
        query_params: dict,
        headers: dict,
        path: str,
        controller: str,
        path_parts: list[str],
        route_info: dict | None = None,
    ):
        # Базовые данные запроса (используются в контроллерах и в шаблонах).
        self.server = server                     # переменные окружения CGI
        self.query_params = query_params         # параметры запроса ?a=1&b=2
        self.headers = headers                   # HTTP-заголовки
        self.request_method = server['REQUEST_METHOD']  # GET/POST/PUT/...
        self.path = path                         # чистый путь без query string
        self.controller = controller             # имя контроллера
        self.path_parts = path_parts             # части пути (action, id, ...)

        # Дополнительная информация о маршруте.
        # Она помогает показать пользователю разбор пути /lang/Controller/Action/Id.
        self.route_info = route_info or {}
