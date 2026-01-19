# Питання 40. CGI: ідемпотентність та кешування відповідей

## Коротка відповідь
Ідемпотентність означає, що повторний запит дає той самий ефект. GET/PUT/DELETE — ідемпотентні, POST — ні. Кешування можна описувати через метадані або HTTP-заголовки (Cache-Control, Expires).

## Детально
- **Ідемпотентні:**
  - GET — лише читання.
  - PUT/DELETE — повторне виконання не змінює результат.
- **Неіде-потентні:** POST — створює новий ресурс.
- **Кешування:** у проєкті є REST-meta поле `cache` з часом життя.

## Де в коді
- **Коментарі про ідемпотентність:** `cgi/controllers/order_controller.py` у `do_get/do_post/do_put/do_delete`.【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L124-L166】
- **Meta cache:** `cgi/controllers/controller_rest.py` та `order_controller.py` використовують `RestCache`.【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L31-L44】【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L33-L46】

## Ключевые моменты в коде
- `cgi/controllers/order_controller.py` — комментарии об идемпотентности методов.
- `cgi/controllers/controller_rest.py` — метаданные кеширования (RestCache).

## Куди перейти в коді
- **`cgi/controllers/order_controller.py`** — приклад різних методів і кешу.
- **`cgi/controllers/controller_rest.py`** — загальна структура meta/cache.
