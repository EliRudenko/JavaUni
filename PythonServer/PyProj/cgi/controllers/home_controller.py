####################################################################
# РЕАЛИЗАЦИЯ ТЕМЫ ВОПРОС 32: CGI: динамічний імпорт і типи контролерів (MVC, API)
####################################################################

# КЛЮЧЕВЫЕ МОМЕНТЫ ПО ТЕМАМ (КАПСОМ ДЛЯ БЫСТРОГО ПОИСКА):
# - ВОПРОС 32: КОНТРОЛЛЕР ПОДКЛЮЧАЕТСЯ ДИНАМИЧЕСКИ И ЯВЛЯЕТСЯ ЧАСТЬЮ MVC-ПОДХОДА.
#


import sys
from models.request import CgiRequest

# ДЗ 3, 4 — Аналіз маршруту + метод params в HomeController
# контроллер отвечает за главную страницу и за вывод параметров CGI

class HomeController:

    def __init__(self, request: CgiRequest):
        # Сохраняем объект запроса его параметры, заголовки и т.д
        self.request = request

    def serve(self):
        # Выбор action основан на 2-й части пути после /home/
        # Если ничего не указано считаем, что нужно открыть index
        action = (
            self.request.path_parts[1].lower()
            if len(self.request.path_parts) > 1 and len(self.request.path_parts[1].strip()) > 0
            else 'index'
        )
        controller_action = getattr(self, action)
        controller_action()

    def index(self):
        # Главная страница только ссылки + блок анализа пути запроса
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")
        
        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        with open('./views/home_index.html', 'rt', encoding="utf-8") as f:
            template = f.read()

        # HTML для анализа маршрута в формате /lang/Controller/Action/Id
        # нужно для ДЗ 3 включить результат анализа в HTML
        # Анализ в виде таблицы
        route_info = self.request.route_info
        if route_info.get("match"):
            analysis_html = f"""
            <table>
                <thead>
                    <tr><th>Поле</th><th>Значение</th></tr>
                </thead>
                <tbody>
                    <tr><td>lang</td><td>{route_info.get("lang")}</td></tr>
                    <tr><td>controller</td><td>{route_info.get("controller")}</td></tr>
                    <tr><td>action</td><td>{route_info.get("action")}</td></tr>
                    <tr><td>id</td><td>{route_info.get("id")}</td></tr>
                </tbody>
            </table>
            """
            analysis_note = "Маршрут соответствует формату /lang/Controller/Action/Id."
        else:
            analysis_html = "<p><i>Шаблон /lang/Controller/Action/Id не распознан.</i></p>"
            analysis_note = "Маршрут не соответствует формализму."

        # Подставляем готовые фрагменты в шаблон
        body = template.format(analysis_note=analysis_note, analysis_html=analysis_html)
        print(layout.replace("<!-- RenderBody -->", body))

    def privacy(self):
        # Статическая страница, просто отдаем заранее подготовленный HTML.
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")

        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        with open('./views/home_privacy.html', 'rt', encoding="utf-8") as f:
            body = f.read()

        print(layout.replace("<!-- RenderBody -->", body))

    def params(self):
        # Вывод всех параметров, которые передал диспетчер доступа CGI
        sys.stdout.buffer.write(b"Content-Type: text/html; charset=utf-8\n\n")

        with open('./views/_layout.html', 'rt', encoding="utf-8") as f:
            layout = f.read()

        # списки, чтобы удобно просматривать данные
        envs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.server.items()) + "</ul>\n"
        hdrs = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.headers.items()) + "</ul>\n"
        qp = "<ul>\n" + "".join(f"<li>{k} = {v}</li>\n" for k, v in self.request.query_params.items()) + "</ul>\n"

        with open('./views/home_params.html', 'rt', encoding="utf-8") as f:
            template = f.read()

        body = template.format(envs=envs, hdrs=hdrs, qp=qp)

        print(layout.replace("<!-- RenderBody -->", body))
