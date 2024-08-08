import json
import sys
import google.generativeai as genai
from dotenv import load_dotenv
import os

phrase = sys.argv[2]
load_dotenv()
GEMINI_KEY = os.getenv('GEMINI_KEY')

safety_settings = [
    {
        "category": "HARM_CATEGORY_DANGEROUS",
        "threshold": "BLOCK_NONE",
    },
    {
        "category": "HARM_CATEGORY_HARASSMENT",
        "threshold": "BLOCK_NONE",
    },
    {
        "category": "HARM_CATEGORY_HATE_SPEECH",
        "threshold": "BLOCK_NONE",
    },
    {
        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
        "threshold": "BLOCK_NONE",
    },
    {
        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
        "threshold": "BLOCK_NONE",
    },
]
model = genai.GenerativeModel('gemini-1.5-flash', safety_settings=safety_settings)
genai.configure(api_key=GEMINI_KEY)
response = model.generate_content(f"Give me a sentence containing the phrase : {phrase}")
print(response.text)