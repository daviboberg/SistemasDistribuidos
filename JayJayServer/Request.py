import urllib.parse
import json

class Request:

    def __init__(self, method, path, query, header, body):
        self.method = method
        self.path = path
        self.query = query
        self.header = header
        self.body = body

    def getParameters(self):
        uri_parts = self.path.split('/')
        return uri_parts[2:]

    def getQuery(self):
        return urllib.parse.parse_qs(self.query)

    def getBodyAsDict(self):
        json_str = self.body.read(int(self.header['content-length'])).decode("UTF-8")

        return json.loads(json_str)