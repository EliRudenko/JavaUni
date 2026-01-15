import json

j_str = '''{
"x": 123,
"y": 1.23,
"b1": true,
"b2": false,
"n": null,
"a": [1, 2, 3, 4],
"o": {
    "f1": "Значення"
  }
}'''

def main() -> None :
    j = json.loads(j_str)
    for k in j :
        print(f"{k}: {j[k]} ({type(j[k])})")
        #x: 123 (<class 'int'>)
        #y: 1.23 (<class 'float'>)
        #b1: True (<class 'bool'>)
        #b2: False (<class 'bool'>)
        #n: None (<class 'NoneType'>)
        #a: [1, 2, 3, 4] (<class 'list'>)
        #o: {'f1': 'Значення'} (<class 'dict'>)
    json.dump(j, ensure_ascii=False, indent=4, fp=open("j.json", "w", encoding='utf-8'))
    
    try:
        j2 = json.load(open("j.json", "r", encoding='utf-8'))
    except json.decoder.JSONDecodeError as err:
        print("Error read file", err)
    else:
        print(j2)


if __name__ == "__main__" :
    main()