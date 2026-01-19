# Питання 24. CGI: взаємодія з оточенням (environment)

## Коротка відповідь
CGI передає дані про запит через змінні оточення (`REQUEST_METHOD`, `QUERY_STRING`, `REQUEST_URI` тощо). Скрипт читає їх через `os.environ`.

## Детально
- **Типові змінні:**
  - `REQUEST_METHOD` — HTTP-метод (GET/POST/PUT...).
  - `QUERY_STRING` — параметри URL.
  - `REQUEST_URI` — шлях запиту.
  - `HTTP_*` — заголовки (наприклад `HTTP_AUTHORIZATION`).
- **Як читати:** `os.environ.items()` або `os.environ["REQUEST_METHOD"]`.

## Де в коді
- **Повний список environment:** `cgi/index.py` будує HTML-таблицю з `os.environ`.【F:PythonServer/PyProj/cgi/index.py†L15-L43】
- **Вибірка ключових змінних:** `cgi/access_manager.py` читає `REQUEST_URI`, `QUERY_STRING`, `REQUEST_METHOD`.【F:PythonServer/PyProj/cgi/access_manager.py†L40-L50】
- **HTTP-заголовки з environment:** `cgi/access_manager.py` перетворює `HTTP_*` у нормальні заголовки.【F:PythonServer/PyProj/cgi/access_manager.py†L87-L90】

## Ключевые моменты в коде
- `cgi/index.py` — вывод переменных окружения через `os.environ`.
- `cgi/access_manager.py` — чтение окружения для маршрутизации.

## Куди перейти в коді
- **`cgi/index.py`** — демонстрація environment у HTML.
- **`cgi/access_manager.py`** — реальне використання environment у маршрутизації.
