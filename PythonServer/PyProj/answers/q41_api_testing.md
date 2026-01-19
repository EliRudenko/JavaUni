# Питання 41. CGI: тестування API-бекенду — способи, переваги, недоліки

## Коротка відповідь
API можна тестувати вручну (браузер, curl, Postman) або автоматично (unit/integration tests). Для CGI корисні логи сервера і явні HTTP-статуси у відповіді.

## Детально
- **curl:** швидко, просто, можна повторювати запити з різними заголовками.
- **Postman/Insomnia:** зручно для колекцій запитів.
- **Логи Apache:** допомагають бачити статуси та помилки.
- **Автотести:** складніше налаштувати для CGI, але можливі через HTTP-клієнти.

## Де в коді
- **REST-ендпойнти для тестів:** `cgi/controllers/order_controller.py`, `cgi/controllers/user_controller.py`.【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L1-L166】【F:PythonServer/PyProj/cgi/controllers/user_controller.py†L1-L102】
- **Логи сервера:** `cgi/notes.txt` містить шляхи до error/access логів. 【F:PythonServer/PyProj/cgi/notes.txt†L8-L10】

## Ключевые моменты в коде
- `cgi/controllers/order_controller.py`/`user_controller.py` — точки, которые удобно тестировать API-клиентом.
- `cgi/notes.txt` — упоминание логов как часть процесса тестирования.

## Куди перейти в коді
- **`cgi/controllers/order_controller.py`** — endpoint-и для тестів HTTP-методів.
- **`cgi/notes.txt`** — де дивитись логи для діагностики.
