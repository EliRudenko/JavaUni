from __future__ import annotations
import requests
import logging
from dataclasses import dataclass, field
from typing import Any, List, Dict, Optional, Iterator

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
        try:
            return cls(
                name=data.get("txt", "Unknown"),
                rate=float(data.get("rate", 0.0)),
                abbr=data.get("cc", "").upper(),
            )
        except (ValueError, TypeError) as e:
            logging.error(f"Ошибка при создании NbuRate: {e} — {data}")
            raise

    def __str__(self) -> str:
        return f"{self.abbr:<5} {self.name:<30} {self.rate:>10.4f}"


class RatesData:
    """Родительский класс: отвечает за загрузку данных с сайта НБУ"""
    __url: str = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json"

    @classmethod
    def fetch_data(cls) -> List[Dict[str, Any]]:
        try:
            response = requests.get(cls.__url, timeout=10)
            response.raise_for_status()
            data = response.json()
            logging.info(f"Fetched {len(data)} rates from NBU")
            return data
        except requests.RequestException as exc:
            logging.error(f"Failed to fetch NBU rates: {exc}")
            raise


@dataclass
class NbuRatesByDate(RatesData):
    date: str = field(init=False)
    rates: List[NbuRate] = field(default_factory=list, init=False)

    def __init__(self, rates_data: List[Dict[str, Any]]):
        """Инициализация с использованием функциональных выражений и сортировки"""
        self.date = rates_data[0].get("exchangedate", "Unknown") if rates_data else "Unknown"
        # Генератор и map
        self.rates = sorted(
            map(NbuRate.from_json, rates_data),
            key=lambda r: r.abbr
        )

    def __str__(self) -> str:
        header = f"Дата: {self.date}\n{'Код':<5} {'Валюта':<30} {'Курс':>10}\n" + "-"*50
        # Используем генератор для строк
        rates_str = "\n".join(str(r) for r in self.rates)
        return f"{header}\n{rates_str}"

    @classmethod
    def fetch_all(cls) -> List[NbuRatesByDate]:
        data = cls.fetch_data()
        grouped: Dict[str, List[Dict[str, Any]]] = {}
        for item in data:
            grouped.setdefault(item.get("exchangedate", ""), []).append(item)
        return list(map(cls, grouped.values()))

    def find_rate(self, abbr: str) -> Optional[NbuRate]:
        abbr_upper = abbr.upper()
        return next((rate for rate in self.rates if rate.abbr == abbr_upper), None)

    def filter_rates(self, search: str) -> Iterator[NbuRate]:
        search_lower = search.lower()
        return (r for r in self.rates if search_lower in r.name.lower() or search_lower in r.abbr.lower())


def main():
    print("Загрузка актуальных курсов НБУ...")
    all_rates = NbuRatesByDate.fetch_all()
    today_rates = all_rates[-1]

    print(f"\nКурсы на дату: {today_rates.date}")

    while True:
        print("\nПоиск курса валют ")
        user_input = input("Введите код или часть названия валюты ('exit' - для выхода) \nКод валюты: ").strip()
        if user_input.lower() == "exit":
            print("Выход из программы.")
            break

        if not user_input:
            print("Ошибка: пустой ввод. Попробуйте снова.")
            continue


        results = list(today_rates.filter_rates(user_input))
        if results:
            print("\nНайденные курсы:")
            print("\n".join(str(r) for r in results))
        else:
            print(f"Курс для '{user_input}' не найден.")


if __name__ == "__main__":
    main()
