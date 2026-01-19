# Питання 39. CGI: архітектура REST, огляд і реалізація в Python

## Коротка відповідь
REST — це стиль побудови веб-сервісів, де ресурси ідентифікуються URL, а дії задаються HTTP-методами. Відповідь зазвичай JSON.

## Детально
- **Ресурсний підхід:** `/order/{id}` — ресурс замовлення.
- **Методи:** GET/POST/PUT/PATCH/DELETE.
- **Статуси:** 200, 201, 204, 404, 405 тощо.
- **Формат відповіді:** JSON із метаданими та даними.

## Де в коді
- **REST-контролер:** `cgi/controllers/controller_rest.py` — база для REST API. 【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L1-L112】
- **REST-сервіс Orders:** `cgi/controllers/order_controller.py` — реалізація CRUD. 【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L1-L166】
- **REST-сервіс Users:** `cgi/controllers/user_controller.py` — авторизація та видача токена. 【F:PythonServer/PyProj/cgi/controllers/user_controller.py†L1-L102】

## Ключевые моменты в коде
- `cgi/controllers/controller_rest.py` — REST-стиль ответов и структура ресурса.
- `cgi/controllers/order_controller.py`/`user_controller.py` — реализация REST-методов для ресурсов.

## Куди перейти в коді
- **`cgi/controllers/controller_rest.py`** — базовий шаблон REST.
- **`cgi/controllers/order_controller.py`** — конкретна реалізація REST-методів.
