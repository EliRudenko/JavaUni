# Питання 33. CGI: JSON-серіалізація користувацьких об’єктів та non-dict типів

## Коротка відповідь
`json.dumps()` вміє працювати зі стандартними типами, але для власних об’єктів потрібен параметр `default=...`, який перетворює об’єкт у словник або рядок.

## Детально
- **Типи за замовчуванням:** dict/list/str/int/float/bool/None.
- **Користувацькі об’єкти:** можна реалізувати `to_json()` або повертати `__dict__`.
- **default:** функція, яка повертає JSON-сумісний об’єкт.

## Де в коді
- **REST-серіалізація:** `cgi/controllers/controller_rest.py` використовує `default=lambda x: x.to_json() ...`.【F:PythonServer/PyProj/cgi/controllers/controller_rest.py†L104-L112】
- **OrderController:** також використовує `default=...` для non-dict типів.【F:PythonServer/PyProj/cgi/controllers/order_controller.py†L110-L120】

## Куди перейти в коді
- **`cgi/controllers/controller_rest.py`** — базова JSON-серіалізація.
- **`cgi/controllers/order_controller.py`** — аналогічна логіка у REST-відповіді.
