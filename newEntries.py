import json
import sys

output_path = sys.argv[1]
number_of_entries = int(input("Number of entries : "))
data = dict()

for i in range(0, number_of_entries):
    print(f"Entry #{i+1}")
    phrase = input("Phrase : ")
    definition = input("Definition : ")
    data[phrase] = definition

with open(output_path, 'w', encoding='utf-8') as file:
    json.dump(data, file, indent=4, ensure_ascii=False)