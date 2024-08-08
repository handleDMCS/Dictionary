import sys
import json
 
file_path = sys.argv[1]
phrase = sys.argv[2]

def get_input(prompt):
    return input(prompt).strip()

newDefinition = get_input("New definition : ")

data = {
    phrase : newDefinition
}

with open(file_path, 'w', encoding='utf-8') as file:
    json.dump(data, file, indent=4, ensure_ascii=False)