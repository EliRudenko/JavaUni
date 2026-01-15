#!C:\Python312\python.exe

DEV_MODE = True

# access manager (диспетчер доступу) - вимога безпеки, яка полягає в
# утворенні єдиної "точки" через яку проходять усі звернення до системи.
# Реалізація зачипає налаштування серверу - спец. файл .htaccess

import io, os, sys
from models.request import CgiRequest

def header_name(hdr:str) -> str :
    '''Convert Apache casing form HEADER_NAME to classic Header-Name'''
    return "-".join(
        s[0].upper() + s[1:].lower() 
        for s in hdr.split('_'))


server = { k:v  for k,v in os.environ.items() if k in 
           ('REQUEST_URI','QUERY_STRING','REQUEST_METHOD') }

# server = {'REQUEST_URI': '/home/privacy','QUERY_STRING': 'htctrl=1','REQUEST_METHOD': 'GET'}

query_params = { k:v  
    for k,v in (item.split('=', 1) if '=' in item else (item, None)
        for item in server['QUERY_STRING'].split('&') ) }

if not 'htctrl' in query_params :
    # порушення контролю проходження запиту через .htaccess
    print('Status: 403 Forbidden')
    print()
    exit()

# чи запит іменем існуючого файлу?
path = server['REQUEST_URI'].split('?', 1)[0]
if not path.endswith('/') and '.' in path :
    # з позиції безпеки слід перевірити шлях на відсутність DT-символів (../)
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


headers = { header_name(k[5:]):v  for k,v in os.environ.items() if k.startswith('HTTP_') }

# Маршрутизація: розділюємо запит на частини /Controller=Home/...
parts = path.split('/', 3)
controller = parts[1] if len(parts) > 1 and len(parts[1].strip()) > 0 else 'Home'
module_name = controller.lower() + '_controller'    # назва файлу контролера без розширення (home_controller)
class_name = controller.capitalize() + 'Controller' # назва класу (HomeController)

# налаштовуємо потік виведення на utf-8
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding="utf-8")

print("Content-Type: application/json; charset=utf-8")


def send_error(message, code=404, phrase="Not Found") :
    print(f"Status: {code} {phrase}\n")
    print("Content-Type: text/html; charset=utf-8")
    print()
    print(message)
    sys.stdout.flush()
    os._exit(0)

# маршрутизація контролерів
sys.path.append("./")   # додаємо поточну директорію як таку, в якій шукаються модулі динамічного імпорту
import importlib        # підключаємо інструменти для динамічного імпорту

try :
    # шукаємо (підключаємо) модуль з іменем module_name
    controller_module = importlib.import_module(f"controllers.{module_name}")

except Exception as ex:
    send_error(f"Controller not found: {module_name} {ex}")

# у ньому знаходимо клас class_name, створюємо з нього об'єкт
controller_class = getattr(controller_module, class_name, None)
if controller_class is None :
    send_error(f"Controller class not found: {module_name}")

controller_object = controller_class(
    CgiRequest(
        server=server,
        query_params=query_params,
        headers=headers,
        path=path,
        controller=controller,
        path_parts=parts[1:]
    )
)
# шукаємо в об'єкті метод serve
serve_action = getattr(controller_object, "serve", None)
if serve_action is None:
    send_error(f"Controller class not found: {module_name}")

# ... та виконуємо його - передаємо управління контролеру
try:
    serve_action()
except Exception as ex:
    message = "Request processing error"
    if DEV_MODE : message += str(ex)
    send_error(message, code=500, phrase="Internal server error")

