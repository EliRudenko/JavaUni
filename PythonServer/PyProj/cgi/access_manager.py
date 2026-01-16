#!C:/Users/morri/AppData/Local/Programs/Python/Python313/python.exe
# ДЗ 3 — Аналіз маршруту /lang/Controller/Action/Id

# центральный диспетчер доступа CGI entry point
# разбор параметров запроса, строится маршрут + подключается соответствующий контроллер
DEV_MODE = True

import os
import sys

from models.request import CgiRequest


sys.stdout.reconfigure(encoding='utf-8')

# access manager диспетчер доступу вимога безпеки
# утворення єдиної "точки" через яку проходять усі звернення до системи
# Реалізація зачипає налаштування серверу  спец. файл .htaccess

# точка входа для каждого HTTP-запроса:
# переменные окружения CGI
# из них базовую маршрутизацию.
# динамически подключаем нужный контроллер


def header_name(hdr:str) -> str :
    # Apache передает заголовки в стиле HEADER_NAME
    # Преобразуем их в привычный вид Header-Name
    return "-".join(
        s[0].upper() + s[1:].lower() 
        for s in hdr.split('_'))


server = { k:v  for k,v in os.environ.items() if k in 
           ('REQUEST_URI','QUERY_STRING','REQUEST_METHOD') }
# request = {'REQUEST_URI': '/','QUERY_STRING': 'htctrl=1'}

# Разбор строки QUERY_STRING в словарь
# в CGI могут быть параметры и без значения
query_params = { k:v  
    for k,v in (item.split('=', 1) if '=' in item else (item, None)
        for item in server['QUERY_STRING'].split('&') ) }

if not 'htctrl' in query_params :
    # Проверка, что запрос действительно прошел через .htaccess
    # Без этого параметра доступ запрещается
    print('Status: 403 Forbidden')
    print()
    exit()


# Визначаємо чистий шлях без query-string
# Далі він використовується і для маршрутизації, і для аналізу /lang/Controller/Action/Id

path = server['REQUEST_URI'].split('?', 1)[0]
if not path.endswith('/') and '.' in path :
    # перевірка шлях на відсутність DT-символів (../)
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


# все HTTP_* переменные окружения и превращаем их в обычные заголовки
headers = { header_name(k[5:]):v  for k,v in os.environ.items() if k.startswith('HTTP_') }


# Анализ адреси у форматі /lang/Controller/Action/Id
# /uk-UA/user/register/123

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

if len(path_segments) >= 4:
    # Беремо тільки перші 4 сегменти за формалізмом
    # Остальные сегменты игнорируем, чтобы не путать анализ
    route_info["lang"] = path_segments[0]
    route_info["controller"] = path_segments[1]
    route_info["action"] = path_segments[2]
    route_info["id"] = path_segments[3]
    route_info["match"] = True


# Маршрутизація якщо виявлено language-префікс
# то контролер визначається після нього

if route_info["match"]:
    # Если маршрут в формате /lang/Controller/Action/Id
    # то контроллер находится во втором сегменте
    routing_segments = path_segments[1:]
else:
    # Если формат не совпал, маршрутизируем как обычно: /controller/action
    routing_segments = path_segments

controller = routing_segments[0] if len(routing_segments) > 0 else 'Home'
module_name = controller.lower() + '_controller'    # назва файлу контролера без розширення (home_controller)
class_name = controller.capitalize() + 'Controller' # назва класу (HomeController)

def send_error(message, code=404, phrase="Not Found"):
    # Единый способ вернуть ошибку в формате "text/plain"
    print(f"Status: {code} {phrase}\n")
    print("Content-Type: text/plain; charset=utf-8\n")
    print()
    print(message)
    sys.stdout.flush()
    os._exit(0)

sys.path.append("./")   # додаємо поточну директорію як таку, в якій шукаються модулі динамічного імпорту
import importlib        # підключаємо інструменти для динамічного імпорту

try :
    # шукаємо (підключаємо) модуль з іменем module_name
    controller_module = importlib.import_module(f"controllers.{module_name}")
except Exception as ex :
    send_error(f"Controller module '{module_name}' not found. {str(ex)}")

controller_class = getattr(controller_module, class_name, None)
if controller_class is None :
    send_error(f"Controller class '{class_name}' not found in module '{module_name}'")

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
controller_action = getattr(controller_object, "serve", None)
if controller_action is None :
    send_error(f"Controller action 'serve' not found in controller '{class_name}'")

try:
    controller_action()
except Exception as ex :
    message = "Request processing error "
    if DEV_MODE:
        message += str(ex)
    send_error(message, code=500, phrase="Internal Server Error")
finally:
    sys.stdout.flush()
