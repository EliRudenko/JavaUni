# requests використовується для HTTP-запиту (отримання JSON).
import requests


# Клас-модель, що відображає JSON-дані у поля Python-об'єкта (ORM-підхід).
class NbuRate:
    def __init__(self, j:dict):
        self.r030 = j["r030"]  # Код валюти НБУ.
        self.name = j["txt"]   # Назва валюти.
        self.rate = j["rate"]  # Курс.
        self.abbr = j["cc"]    # Скорочення.
    
    def __str__(self) -> str:
        return f"{self.name} ({self.abbr}): {self.rate}"
    

# Базовий клас для зберігання даних про курси.
class RatesData:
    def __init__(self):
        self.exchange_date = None  # Дата курсу.
        self.rates = []            # Список об'єктів NbuRate.

    


# Похідний клас, який заповнює дані з API.
class NbuRatesData(RatesData):
    url = 'https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json'

    def __init__(self):
        request = requests.get(NbuRatesData.url)  # HTTP-запит.
        response = request.json()                 # JSON -> Python.
        '''Курси валют за даними НБУ'''
        self.exchange_date = response[0]["exchangedate"]  # Поле з JSON.
        self.rates = [NbuRate(r) for r in response]        # Масив JSON -> масив об'єктів.



def main():
    rates_data:RatesData = NbuRatesData()  # Поліморфізм: змінна базового типу.

    name = input("Введіть назву валюти або її частину (наприклад, долар, євро): ")
    results = [rate for rate in rates_data.rates
               if name in rate.name]  # Фільтрація списку.
    
    if results:
        print(f"Знайдено {len(results)} валют(у):")
        for rate in results:
            print(rate)
    else:
        print(f"Валюта з назвою '{name}' не знайдена.")

    
    # abbr = input("Введіть скорочену назву валюти (наприклад, USD, EUR): ").upper()

    # results = [rate for rate in rates_data.rates
    #            if abbr in rate.abbr]
    # if results:
    #     print(f"Знайдено {len(results)} валют(у):")
    #     for rate in results:
    #         print(rate)
    # else:
    #     print(f"Валюта з '{abbr}' не знайдена.")

    # print(next((rate for rate in rates_data.rates if rate.abbr == abbr), f"Валюта '{abbr}' не знайдена."))
    # for rate in rates_data.rates:
    #     if rate.abbr == abbr:
    #         print(rate)
    #         break
    # else:
    #     print(f"Валюта '{abbr}' не знайдена.")


if __name__ == "__main__":
    main()
