# Питання 34. CGI: концепція DAL (Data Access Layer)

## Коротка відповідь
DAL — це шар доступу до даних, який відокремлює SQL та роботу з БД від логіки контролерів. Це робить код чистішим і легшим для підтримки.

## Детально
- **Переваги:** централізація доступу до БД, повторне використання, ізоляція змін схеми.
- **Як організовано:** клас `DataAccessor` інкапсулює підключення, SQL-запити, хешування паролів.

## Де в коді
- **DAL реалізація:** `cgi/dao/data_accessor.py`.【F:PythonServer/PyProj/cgi/dao/data_accessor.py†L1-L203】
- **Використання DAL у контролері:** `cgi/controllers/user_controller.py` викликає `DataAccessor.authenticate()`.【F:PythonServer/PyProj/cgi/controllers/user_controller.py†L74-L83】

## Ключевые моменты в коде
- `cgi/dao/data_accessor.py` — DAL/DAO как отдельный слой доступа к данным.
- `cgi/controllers/user_controller.py` — использование DAL из контроллера.

## Куди перейти в коді
- **`cgi/dao/data_accessor.py`** — приклад DAL-шару.
- **`cgi/controllers/user_controller.py`** — виклик DAL із контролера.
