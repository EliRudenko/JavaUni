# Шебанг: шлях до Python для CGI.
#!C:/Users/morri/AppData/Local/Programs/Python/Python313/python.exe
# ДЗ 3 — Аналіз маршруту /lang/Controller/Action/Id

# Центральний диспетчер доступу (CGI entry point).
# Ідея: усі запити проходять через одну "точку" — front controller.
# Тут розбираємо запит, формуємо маршрут і динамічно підключаємо контролер.
DEV_MODE = True

# os дозволяє читати змінні оточення CGI.
import os
# sys потрібен для stdout і завершення процесу.
import sys

# CgiRequest — контейнер для даних запиту.
from models.request import CgiRequest


# Налаштовуємо UTF-8 для коректного виводу.
# У CGI відповідь пишемо в stdout, тому кодування має бути коректним.
sys.stdout.reconfigure(encoding='utf-8')

# access manager диспетчер доступу вимога безпеки
# утворення єдиної "точки" через яку проходять усі звернення до системи
# Реалізація зачипає налаштування серверу  спец. файл .htaccess

# точка входа для каждого HTTP-запроса:
# переменные окружения CGI
# из них базовую маршрутизацию.
# динамически подключаем нужный контроллер


# Перетворює CGI-назви заголовків у стандартний вигляд.
# Apache передає заголовки в змінних HTTP_HEADER_NAME.
# Ми повертаємо їх до звичного Header-Name.
def header_name(hdr:str) -> str :
    # Apache передає заголовки як HTTP_HEADER_NAME.
    # Преобразуємо їх у формат Header-Name.
    return "-".join(
        s[0].upper() + s[1:].lower()
        for s in hdr.split('_'))


# Беремо лише потрібні CGI-змінні (мінімальний набір).
# Це приклад читання оточення (environment) від сервера:
# REQUEST_METHOD, REQUEST_URI, QUERY_STRING тощо.
server = { k:v  for k,v in os.environ.items() if k in
           ('REQUEST_URI','QUERY_STRING','REQUEST_METHOD') }
# request = {'REQUEST_URI': '/','QUERY_STRING': 'htctrl=1'}

# Разбор строки QUERY_STRING в словарь
# в CGI могут быть параметры и без значения
# Розбір рядка QUERY_STRING у словник.
# Це демонструє, як сервер передає параметри GET через environment.
query_params = { k:v
    for k,v in (item.split('=', 1) if '=' in item else (item, None)
        for item in server['QUERY_STRING'].split('&') ) }

# Перевірка, що запит пройшов через .htaccess (захисний параметр).
# Якщо параметра немає — забороняємо доступ (403).
if not 'htctrl' in query_params :
    print('Status: 403 Forbidden')
    print()
    exit()


# Визначаємо чистий шлях без query-string
# Далі він використовується і для маршрутизації, і для аналізу /lang/Controller/Action/Id

# Визначаємо чистий шлях без query-string.
path = server['REQUEST_URI'].split('?', 1)[0]
# Якщо шлях вказує на статичний файл, віддаємо його напряму.
# Це спосіб "ручного" сервінгу статики через CGI (повільніше, ніж напряму Apache).
if not path.endswith('/') and '.' in path :
    # Перевірка файлу за розширенням.
    ext = path[(path.rindex('.') + 1):]
    allowed_media_types = {
        'png': 'image/png',
        'jpg': 'image/jpeg',
        'css': 'text/css',
        'js' : 'text/javascript',
    }
    if ext in allowed_media_types :
        try :
            with open(os.path.abspath('./static/') + path, mode='rb') as file :
                sys.stdout.buffer.write(
                    f"Content-Type: {allowed_media_types[ext]}\n\n".encode()
                )
                sys.stdout.buffer.write(file.read())
                sys.stdout.flush()
            os._exit(0)
        except :
            pass
    else:
        # Якщо розширення не підтримується, повертаємо 415 Unsupported Media Type.
        # Це приклад обмеження доступу до статики за медіа-типами.
        print("Status: 415 Unsupported Media Type\n")
        print("Content-Type: text/plain; charset=utf-8\n")
        print()
        print(f"Unsupported static resource type: .{ext}")
        sys.stdout.flush()
        os._exit(0)


# все HTTP_* переменные окружения и превращаем их в обычные заголовки
# Отримуємо HTTP-заголовки з CGI-змінних.
# У CGI заголовки приходять як HTTP_HEADER_NAME.
headers = { header_name(k[5:]):v  for k,v in os.environ.items() if k.startswith('HTTP_') }


# Анализ адреси у форматі /lang/Controller/Action/Id
# /uk-UA/user/register/123

# Розбиваємо шлях на сегменти.
# Це основа маршрутизації: /controller/action/id.
path_segments = [segment for segment in path.split('/') if segment]
route_info = {
    "raw": path,
    "segments": path_segments,
    "match": False,
    "lang": None,
    "controller": None,
    "action": None,
    "id": None,
}

# Якщо формат /lang/Controller/Action/Id відповідає, заповнюємо дані.
# Це демонструє альтернативну схему маршрутизації з префіксом мови.
if len(path_segments) >= 4:
    # Беремо тільки перші 4 сегменти.
    route_info["lang"] = path_segments[0]
    route_info["controller"] = path_segments[1]
    route_info["action"] = path_segments[2]
    route_info["id"] = path_segments[3]
    route_info["match"] = True


# Маршрутизація якщо виявлено language-префікс
# то контролер визначається після нього

# Обираємо сегменти для маршрутизації.
# Якщо є language-префікс, то контролер у другому сегменті.
if route_info["match"]:
    routing_segments = path_segments[1:]  # Пропускаємо lang.
else:
    routing_segments = path_segments

# Визначаємо контролер і імена модуля/класу.
# Далі будемо імпортувати контролер динамічно.
controller = routing_segments[0] if len(routing_segments) > 0 else 'Home'
module_name = controller.lower() + '_controller'    # назва файлу контролера без розширення (home_controller)
class_name = controller.capitalize() + 'Controller' # назва класу (HomeController)

# Єдиний метод для відправки помилки у CGI-відповіді.
# Тут явно формуємо HTTP-статус і заголовки.
def send_error(message, code=404, phrase="Not Found"):
    print(f"Status: {code} {phrase}\n")
    print("Content-Type: text/plain; charset=utf-8\n")
    print()
    print(message)
    sys.stdout.flush()
    os._exit(0)

# Додаємо поточну директорію, щоб importlib міг знайти модулі контролерів.
# Динамічний імпорт — спосіб гнучко вибирати контролер (MVC, REST тощо).
sys.path.append("./")
import importlib        # Інструменти для динамічного імпорту.

# Динамічний імпорт контролера.
# Якщо модуль не знайдено — повертаємо 404.
try :
    controller_module = importlib.import_module(f"controllers.{module_name}")
except Exception as ex :
    send_error(f"Controller module '{module_name}' not found. {str(ex)}")

# Знаходимо клас контролера в модулі.
# Якщо клас відсутній — повертаємо 404.
controller_class = getattr(controller_module, class_name, None)
if controller_class is None :
    send_error(f"Controller class '{class_name}' not found in module '{module_name}'")

# Створюємо об'єкт контролера та передаємо CgiRequest.
# Це "контейнер" усіх даних запиту (environment, headers, route info).
controller_object = controller_class(
    CgiRequest(
        server=server,
        query_params=query_params,
        headers=headers,
        path=path,
        controller=controller,
        path_parts=[controller] + routing_segments[1:],
        route_info=route_info,
    )
)
# Очікуємо, що контролер має метод serve.
# Це уніфікує виклик для різних типів контролерів (MVC, REST).
controller_action = getattr(controller_object, "serve", None)
if controller_action is None :
    send_error(f"Controller action 'serve' not found in controller '{class_name}'")

# Виконуємо дію контролера і обробляємо помилки.
# DEV_MODE дає деталі помилки, у production краще приховувати.
try:
    controller_action()
except Exception as ex :
    message = "Request processing error "
    if DEV_MODE:
        message += str(ex)
    send_error(message, code=500, phrase="Internal Server Error")
finally:
    sys.stdout.flush()
