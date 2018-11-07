
class Request:

    def __init__(self, method, uri, header, body):
        self.method = method
        self.uri = uri
        self.header = header
        self.body = body


