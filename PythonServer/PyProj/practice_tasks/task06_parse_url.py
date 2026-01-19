from urllib.parse import urlparse  # модуль для розбору URL


class Helper:  # клас для роботи з URL-адресами
    def parse_url(self, url: str) -> dict:  # метод повертає dict зі складниками URL
        parsed = urlparse(url)  # виконуємо стандартний розбір URL
        result = {  # формуємо словник результатів
            "scheme": parsed.scheme,  # протокол (http/https)
            "host": parsed.hostname,  # хост або домен
            "port": parsed.port,  # порт (якщо є)
            "path": parsed.path,  # шлях
            "query": parsed.query,  # query-рядок
            "fragment": parsed.fragment,  # фрагмент
        }  # завершуємо формування dict
        return result  # повертаємо результат


def main() -> None:  # головна функція демонстрації
    helper = Helper()  # створюємо екземпляр Helper
    sample_url = "https://example.com:8080/orders/list/42?paid=true#section"  # приклад URL
    result = helper.parse_url(sample_url)  # розбираємо URL
    print("Parsed URL:", result)  # виводимо результат


if __name__ == "__main__":  # перевірка прямого запуску
    main()  # запускаємо демонстрацію
