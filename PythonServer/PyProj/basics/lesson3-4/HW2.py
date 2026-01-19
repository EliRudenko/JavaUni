import random
from functools import reduce

data = tuple(random.randint(1, 100) for _ in range(10))
print(f"Данные: {data}")

strategies = {
    "арифметическое": lambda nums: sum(nums) / len(nums),
    "геометрическое": lambda nums: reduce(lambda x, y: x * y, nums) ** (1/len(nums)),
    "гармоническое": lambda nums: len(nums) / sum(1/x for x in nums)
}

results = {name: func(data) for name, func in strategies.items()}

for name, value in results.items():
    print(f"{name} среднее: {value:.2f}")

def choose_max_result(results_dict):
    max_method = max(results_dict, key=results_dict.get)
    return max_method, results_dict[max_method]

max_name, max_value = choose_max_result(results)
print(f"\nmax: {max_name} -> {max_value:.2f}")
