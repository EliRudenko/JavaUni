# Питання 23. CGI: налаштування сервера для запуску Python-скриптів, вимоги до скриптів

## Коротка відповідь
Для CGI потрібен веб-сервер (наприклад, Apache) з дозволеним ExecCGI та handler для `.py`. Скрипт має містити **shebang**, права на виконання та повертати HTTP-заголовки у stdout.

## Детально
- **Налаштування Apache:**
  - `Options +ExecCGI` — дозволяє запуск CGI.
  - `AddHandler cgi-script .py` — визначає Python-скрипти як CGI.
  - `DirectoryIndex index.py` — стартовий файл.
- **Вимоги до скриптів:**
  - шебанг з шляхом до інтерпретатора;
  - коректні HTTP-заголовки (Content-Type, Status);
  - вивід через stdout.

## Де в коді
- **Apache VirtualHost (конфіг):** `cgi/notes.txt` — приклад налаштування локального хостингу з ExecCGI та handler.【F:PythonServer/PyProj/cgi/notes.txt†L1-L22】
- **Shebang у CGI-скриптах:** `cgi/index.py`, `cgi/access_manager.py`.【F:PythonServer/PyProj/cgi/index.py†L1-L2】【F:PythonServer/PyProj/cgi/access_manager.py†L1-L2】

## Куди перейти в коді
- **`cgi/notes.txt`** — приклад конфігурації Apache.
- **`cgi/index.py`** — мінімальний CGI-скрипт.
