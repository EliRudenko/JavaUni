# 
start = int(input("from: "))
end = int(input("to: "))
step = int(input("step: "))

# check
if start == end:
    print("start and end cannot be the same")
elif step <= 0:
    print("step must be positive num")
else:

    if start < end:
        current = start
        while current <= end:
            print(current, end=" ")
            current += step
    else:
        current = start
        while current >= end:
            print(current, end=" ")
            current -= step
