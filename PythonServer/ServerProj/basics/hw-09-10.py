import math

average_methods = (
    lambda data: sum(data) / len(data),
    lambda data: math.pow(
        math.prod(data), 1 / len(data)
    ),  
    lambda data: len(data) / sum(1 / x for x in data),  
)

numbers = (2, 4, 6, 8, 10)

def getBestAverage(data, methods):
    results = []
    for method in methods:
        result = method(data)
        results.append(result)
    max_value = max(results)
    max_index = results.index(max_value)
    return max_index, max_value, results

index, max_result, all_results = getBestAverage(numbers, average_methods)

print("Дані:", numbers)
print("Арифметичне середнє:", round(all_results[0], 4))
print("Геометричне середнє:", round(all_results[1], 4))
print("Гармонійне середнє:", round(all_results[2], 4))
print()
print("Найбільше середнє:", round(max_result, 4))
