# Питання 32. CGI: динамічний імпорт і типи контролерів (MVC, API)

## Коротка відповідь
Динамічний імпорт дозволяє підвантажувати контролер за ім'ям з URL. У проєкті є MVC-контролери (HTML) та REST-контролери (JSON API).

## Детально
- **Динамічний імпорт:** `importlib.import_module()` дає гнучкість і скорочує жорсткі зв’язки.
- **MVC-контролери:** повертають HTML (наприклад, HomeController).
- **REST-контролери:** повертають JSON, використовують status/meta/data (OrderController, UserController).

## Де в коді
- **Динамічний імпорт:** `cgi/access_manager.py` формує `module_name` і імпортує контролер. 【F:PythonServer/PyProj/cgi/access_manager.py†L126-L152】
- **MVC-приклад:** `cgi/controllers/home_controller.py` рендерить HTML. 【F:PythonServer/PyProj/cgi/controllers/home_controller.py†L1-L90】
- **REST-приклад:** `cgi/controllers/order_controller.py` повертає JSON-структуру. 【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L1-L166】

## Ключевые моменты в коде
- `cgi/access_manager.py` — динамический импорт контроллеров.
- `cgi/controllers/home_controller.py` и `order_controller.py` — примеры контроллеров (MVC/API).

## Куди перейти в коді
- **`cgi/access_manager.py`** — динамічне підключення контролера.
- **`cgi/controllers/home_controller.py`** — HTML/MVC.
- **`cgi/controllers/order_controller.py`** — REST API.
