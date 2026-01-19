####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 21: Робота серверів у режимі CGI (Common Gateway Interface)
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 21: КЛАСС CgiRequest СОБИРАЕТ ДАННЫЕ CGI-ЗАПРОСА В ОДНУ СТРУКТУРУ.
#


# Контейнер даних CGI-запиту для передачі між контролером і шаблоном.
# Тут зібрано все, що CGI передає через environment, + розбір маршруту.
class CgiRequest:
    # ДЗ 3: об'єкт запиту з результатом аналізу маршруту.
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
        # Базові дані запиту використовуються в контролерах і шаблонах.
        # server: мінімальний набір environment-змінних.
        self.server = server                     # Змінні оточення CGI.
        self.query_params = query_params         # Параметри запиту ?a=1&b=2.
        self.headers = headers                   # HTTP-заголовки.
        self.request_method = server['REQUEST_METHOD']  # Метод запиту (GET/POST/...).
        self.path = path                         # Чистий шлях без query string.
        self.controller = controller             # Ім'я контролера.
        self.path_parts = path_parts             # Частини шляху (action, id, ...).

        # Додаткова інформація про маршрут /lang/Controller/Action/Id.
        # route_info допомагає відображати аналіз маршруту у HTML.
        self.route_info = route_info or {}
