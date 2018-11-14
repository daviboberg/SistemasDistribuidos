import urllib.parse
import json

# Requets class. Basic a data structure
class Request:

    def __init__(self, method, path, query, header, body):
        self.method = method
        self.path = path
        self.query = query
        self.header = header
        self.body = body

    # Get the list of parameters from path
    def getParameters(self):
        uri_parts = self.path.split('/')
        return uri_parts[2:]
    # Get a dictionary from the queries
    def getQuery(self):
        return urllib.parse.parse_qs(self.query)
    # Read the body, turn its json to a dictionary
    def getBodyAsDict(self):
        json_str = self.body.read(int(self.header['content-length'])).decode("UTF-8")

        return json.loads(json_str)