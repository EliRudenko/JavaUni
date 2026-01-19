# Питання 35. CGI: інсталяція та сідування БД, ролі, адміністратори

## Коротка відповідь
Інсталяція — створення таблиць, сідування — початкові дані (ролі, admin). Ролі керують правами (create/read/update/delete), адміністратор має всі права.

## Детально
- **Інсталяція:** створення `users`, `roles`, `accesses`, `tokens`.
- **Сідування:** додаємо ролі `admin` і `user`, створюємо початкового користувача admin.

## Де в коді
- **Створення таблиць:** `cgi/dao/data_accessor.py` метод `install()` та `_install_*`.【F:PythonServer/PyProj/cgi/dao/data_accessor.py†L24-L113】
- **Сідування ролей/користувача:** `_seed_roles()`, `_seed_users()`.【F:PythonServer/PyProj/cgi/dao/data_accessor.py†L118-L188】

## Ключевые моменты в коде
- `cgi/dao/data_accessor.py` — подготовка БД, сидирование и роли пользователей.

## Куди перейти в коді
- **`cgi/dao/data_accessor.py`** — інсталяція та сідування БД.
