# Питання 31. CGI: огляд способів і традицій маршрутизації

## Коротка відповідь
Маршрутизація — це відповідність URL -> контролер/дія. Поширені підходи: **path-based** (`/controller/action`), **query-based** (`?controller=...`), а також префікси (наприклад, `/<lang>/controller/...`).

## Детально
- **Path-based routing:** чисті URL, зручні для SEO.
- **Query-based routing:** простіше реалізувати, але менш читабельні.
- **Prefix routing:** додаткові сегменти для мови/версії API.

## Де в коді
- **Path-based:** `cgi/access_manager.py` розбирає `/controller/action`.【F:PythonServer/PyProj/cgi/access_manager.py†L95-L129】
- **Prefix routing:** `cgi/access_manager.py` підтримує формат `/lang/Controller/Action/Id`.【F:PythonServer/PyProj/cgi/access_manager.py†L95-L125】

## Ключевые моменты в коде
- `cgi/access_manager.py` — разбор маршрута и выбор контроллера.

## Куди перейти в коді
- **`cgi/access_manager.py`** — блок розбору маршруту.
