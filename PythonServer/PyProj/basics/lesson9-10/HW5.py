from __future__ import annotations
import requests
import logging
from dataclasses import dataclass, field
from typing import Any, List, Dict, Optional, Iterator
from datetime import datetime

LOG_FILE: str = "nbu_rates.log"

logging.basicConfig(
    filename=LOG_FILE,
    filemode="a",
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
)


@dataclass(frozen=True)
class NbuRate:
    name: str
    rate: float
    abbr: str

    @classmethod
    def from_json(cls, data: Dict[str, Any]) -> NbuRate:
        return cls(
            name=data.get("txt", "Unknown"),
            rate=float(data.get("rate", 0.0)),
            abbr=data.get("cc", "").upper(),
        )

    def __str__(self) -> str:
        return f"{self.abbr:<5} {self.name:<30} {self.rate:>10.4f}"


class RatesData:
    __url: str = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json"

    @classmethod
    def fetch_data(cls, date: Optional[str] = None) -> List[Dict[str, Any]]:
        params = {}
        if date:
            params["date"] = datetime.strptime(date, "%d.%m.%Y").strftime("%Y%m%d")
        response = requests.get(cls.__url, params=params, timeout=10)
        response.raise_for_status()
        return response.json()


@dataclass
class NbuRatesByDate(RatesData):
    date: str = field(init=False)
    rates: List[NbuRate] = field(default_factory=list, init=False)

    def __init__(self, rates_data: List[Dict[str, Any]]):
        self.date = rates_data[0].get("exchangedate", "Unknown") if rates_data else "Unknown"
        self.rates = sorted(map(NbuRate.from_json, rates_data), key=lambda r: r.abbr)

    def __str__(self) -> str:
        header = f"\nДата: {self.date}\n{'Код':<5} {'Валюта':<30} {'Курс':>10}\n" + "-" * 50
        rates_str = "\n".join(str(r) for r in self.rates)
        return f"{header}\n{rates_str}"

    @classmethod
    def for_date(cls, date_str: str) -> NbuRatesByDate:
        data = cls.fetch_data(date_str)
        if not data:
            raise ValueError(f"Нет данных для {date_str}")
        return cls(data)

    def search_rates(self, query: str) -> List[NbuRate]:
        q = query.lower()
        return [r for r in self.rates if q in r.name.lower() or q in r.abbr.lower()]


def validate_date(date_str: str) -> Optional[str]:
    try:
        date_obj = datetime.strptime(date_str, "%d.%m.%Y")
        if date_obj > datetime.today():
            print("Дата из будущего.")
            return None
        return date_str
    except ValueError:
        print("Неверный формат (дд.мм.гггг).")
        return None


def main():
    while True:
        user_date = input("Введите дату (дд.мм.гггг) или 'exit': ").strip()
        if user_date.lower() == "exit":
            break
        valid_date = validate_date(user_date)
        if not valid_date:
            continue
        try:
            rates_obj = NbuRatesByDate.for_date(valid_date)
        except Exception:
            print("Ошибка загрузки данных.")
            continue

        choice = input("Показать все (1) или поиск (2): ").strip()
        if choice == "1":
            print(rates_obj)
        elif choice == "2":
            query = input("Введите код или часть названия валюты: ").strip()
            results = rates_obj.search_rates(query)
            if results:
                print(f"\nДата: {rates_obj.date}\n{'Код':<5} {'Валюта':<30} {'Курс':>10}\n" + "-" * 50)
                print("\n".join(str(r) for r in results))
            else:
                print("Ничего не найдено.")
        else:
            print("Неверный выбор.")


if __name__ == "__main__":
    main()
