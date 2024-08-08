from gtts import gTTS
import os

word = input("text : ")

tts = gTTS(text=word, lang='en')

tts.save("word.mp3")

os.system("start word.mp3")