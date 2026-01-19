# Питання 25. CGI: стандартні потоки вводу/виводу та кодування

## Коротка відповідь
CGI читає тіло запиту зі **stdin**, а відповідь формує через **stdout**. Для коректної роботи з UTF-8 у Python потрібно налаштувати кодування stdout.

## Детально
- **stdin:** використовується для даних POST/PUT (у цьому проєкті не парситься, але потік доступний).
- **stdout:** саме сюди пишуться HTTP-заголовки і тіло відповіді.
- **Кодування:** `sys.stdout.reconfigure(encoding='utf-8')` гарантує, що кирилиця не зламається.

## Де в коді
- **Налаштування stdout:** `cgi/index.py` і `cgi/access_manager.py` задають UTF-8 для stdout.【F:PythonServer/PyProj/cgi/index.py†L12-L14】【F:PythonServer/PyProj/cgi/access_manager.py†L18-L19】
- **Вивід відповіді у stdout:** `cgi/index.py`, `cgi/controllers/controller_rest.py`, `cgi/controllers/order_controller.py`.【F:PythonServer/PyProj/cgi/index.py†L47-L60】【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L90-L112】【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L100-L121】

## Куди перейти в коді
- **`cgi/index.py`** — мінімальний приклад запису заголовків/HTML.
- **`cgi/controllers/order_controller.py`** — приклад JSON-відповіді через stdout.
