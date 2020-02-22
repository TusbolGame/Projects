from django.conf import settings
import requests

def search(query):

    key = settings.GOOGLE_SEARCH_API_KEY

    return query
