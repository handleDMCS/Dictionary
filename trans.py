import sys
from googletrans import Translator

def translate(text, lang):
    translator = Translator()
    translation = translator.translate(text, dest=lang)
    return translation.text

text = input("The original text : ")

translation = translate(text, sys.argv[2])

print("Translation : ", translation)