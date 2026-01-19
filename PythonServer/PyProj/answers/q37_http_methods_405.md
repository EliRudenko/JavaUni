# Питання 37. CGI: маршрутизація за HTTP-методами, семантика, статус 405

## Коротка відповідь
У REST API метод визначає дію: `GET` — читання, `POST` — створення, `PUT` — заміна, `PATCH` — часткова зміна, `DELETE` — видалення. Якщо метод не підтримується — 405 Method Not Allowed.

## Детально
- **Маршрутизація:** формуємо назву методу як `do_<method>`.
- **405:** стандартна відповідь при невідомому методі.

## Де в коді
- **Router за методом:** `cgi/controllers/controller_rest.py` та `cgi/controllers/order_controller.py`.【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L90-L104】【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L82-L98】
- **Відповідь 405:** встановлюється у `RestStatus.status405`.【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L27-L29】【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L23-L24】

## Ключевые моменты в коде
- `cgi/controllers/controller_rest.py` — возврат 405 при неверных HTTP-методах.
- `cgi/controllers/order_controller.py` — do_get/do_post/do_put/do_delete как маршрутизация по методу.

## Куди перейти в коді
- **`cgi/controllers/controller_rest.py`** — базовий REST-контролер.
- **`cgi/controllers/order_controller.py`** — конкретна реалізація методів.
