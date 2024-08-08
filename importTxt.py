import json
import re
import sys
from googletrans import Translator

output_path = sys.argv[1]
input_path = sys.argv[2]

def translate(text, lang):
    translator = Translator()
    translation = translator.translate(text, dest=lang)
    return translation.text

def read_file(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            return file.read()
    except FileNotFoundError:
        print(f"Error: The file {file_path} was not found.")
        return None
    except IOError:
        print(f"Error: There was an issue reading the file {file_path}.")
        return None

def get_unique_words(paragraph):
    no_punct_num = re.sub(r'[^\w\s]|\d', '', paragraph)
    lowercase = no_punct_num.lower()
    unique_words = set(lowercase.split())
    return unique_words

content = read_file(input_path)

if content is not None:
    data = dict()
    unique_words = get_unique_words(content)
    phrase_cnt = 0
    for word in unique_words:
        trans = translate(word, 'vi').lower()
        if(trans != word):
            phrase_cnt += 1
            data[word] = trans
            print(">", phrase_cnt, "keyword(s) found.", end='\r')
    print()
    with open(output_path, 'w', encoding='utf-8') as file:
        json.dump(data, file, indent=4, ensure_ascii=False)