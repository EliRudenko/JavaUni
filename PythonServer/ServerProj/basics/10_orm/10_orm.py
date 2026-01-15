import requests
from datetime import datetime

class NbuRate:
    def __init__(self, j: dict):
        self.r030 = j["r030"]
        self.name = j["txt"]
        self.rate = j["rate"]
        self.abbr = j["cc"]
        self.date = j["exchangedate"]

    def __str__(self):
        return f"{self.abbr} ({self.name}) {self.rate:.4f}"


class RatesData:
    def __init__(self):
        self.exchange_date = None
        self.rates = []


class NbuRatesData(RatesData):
    base_url = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange"

    def __init__(self, date: str = None):
        params = {"json": ""}
        if date:
            params["date"] = date
        response = requests.get(NbuRatesData.base_url, params=params).json()

        if not response:
            self.exchange_date = None
            self.rates = []
        else:
            self.exchange_date = response[0]["exchangedate"]
            self.rates = [NbuRate(r) for r in response]


def main():
    temp_date = input("Input exchange date (dd.mm.yyyy): ").strip()

    try:
        day, month, year = map(int, temp_date.split('.'))
        user_date = datetime(year, month, day)
    except Exception:
        raise ValueError("Correct format for date: dd.mm.yyyy")

    today = datetime.now()
    if user_date > today:
        raise ValueError("Date must be in the past")

    api_date = user_date.strftime("%Y%m%d")

    rates_data = NbuRatesData(date=api_date)

    if not rates_data.rates:
        print("No data available for this date")
        return

    for rate in sorted(rates_data.rates, key=lambda x: x.abbr):
        print(rate)


if __name__ == '__main__':
    main()
