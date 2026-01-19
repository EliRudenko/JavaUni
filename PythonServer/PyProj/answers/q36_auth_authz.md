# Питання 36. CGI: аутентифікація та авторизація. Заголовок Authorization

## Коротка відповідь
Аутентифікація — хто ти, авторизація — що тобі можна. У CGI заголовок **Authorization** може бути втрачений без `CGIPassAuth on`, тому це треба налаштувати в .htaccess.

## Детально
- **Basic Auth:** заголовок `Authorization: Basic <base64(login:password)>`.
- **Bearer (JWT):** `Authorization: Bearer <token>`.
- **CGIPassAuth:** дозволяє передавати Authorization у CGI.

## Де в коді
- **Передача Authorization у CGI:** `cgi/.htaccess` — директива `CGIPassAuth on`.【F:PythonServer/PyProj/cgi/.htaccess†L1-L2】
- **Basic Auth:** `cgi/controllers/user_controller.py` читає Authorization і декодує Base64. 【F:PythonServer/PyProj/cgi/controllers/user_controller.py†L33-L83】
- **Bearer JWT:** `cgi/dao/helper.py` витягує Bearer токен. 【F:PythonServer/PyProj/cgi/dao/helper.py†L63-L74】

## Ключевые моменты в коде
- `cgi/controllers/user_controller.py` — проверка заголовка Authorization.
- `cgi/dao/helper.py` — вспомогательные проверки токена/ролей.

## Куди перейти в коді
- **`cgi/.htaccess`** — важлива директива для Authorization.
- **`cgi/controllers/user_controller.py`** — приклад Basic Auth.
- **`cgi/dao/helper.py`** — приклад Bearer JWT.
