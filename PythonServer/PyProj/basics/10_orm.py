import requests


class NbuRate:
    def __init__(self, j:dict):
        self.r030 = j["r030"]
        self.name = j["txt"]
        self.rate = j["rate"]
        self.abbr = j["cc"]
    
    def __str__(self) -> str:
        return f"{self.name} ({self.abbr}): {self.rate}"
    

class RatesData:
    def __init__(self):
        self.exchange_date = None
        self.rates = []

    


class NbuRatesData(RatesData):
    url = 'https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json'

    def __init__(self):
        request = requests.get(NbuRatesData.url)
        response = request.json()
        '''Курси валют за даними НБУ'''
        self.exchange_date = response[0]["exchangedate"]
        self.rates = [NbuRate(r) for r in response]



def main():
    rates_data:RatesData = NbuRatesData()

    name = input("Введіть назву валюти або її частину (наприклад, долар, євро): ")
    results = [rate for rate in rates_data.rates
               if name in rate.name]
    
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