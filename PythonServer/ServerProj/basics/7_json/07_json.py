# JSON

import json

j_str = '''{
"x": 123,
"y": 1.23,
"b1": true,
"b2": false,
"n": null,
"a": [1,2,3],
"o": {
    "f1": "value"
    }
}'''


def main():
   j =  json.loads(j_str)
   print(type(j), j)
   for k in j :
       print(k, type(j[k]), j[k])
   j_str2 = json.dumps(j)
   print(j_str2)
   #json.dump(j, ensure_ascii=False, indent=4, fp=open("j.json", "w", encoding='UTF-8'))
   try:
        j2 = json.load(fp=open("j.json", "r", encoding='UTF-8'))
   except json.decoder.JSONDecodeError as err:
       print("Decode error: ", err)
   else:  
       print(j2)
    


if __name__ == '__main__':
    main()
'''
JavaScript Object Notation
string:
    "The String"
values:
    123
    0.123
    true
    false
    null
array:
    [1,2,3,4]
object:
    {
    "fieldname": "field value", 
    "fieldname2": "field value 2"
    }
'''