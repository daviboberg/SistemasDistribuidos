import time
import urllib.parse

from Response import Response
from Request import Request
from Router import Router
from http.server import BaseHTTPRequestHandler, HTTPServer

HOST_NAME = 'localhost'
PORT_NUMBER = 8600


class MyHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.processMethod('get')

    def do_POST(self):
        self.processMethod('post')

    def do_PUT(self):
        self.processMethod('put')

    def do_DELETE(self):
        self.processMethod('delete')

    def processMethod(self, method):
        parse = urllib.parse.urlparse(self.path)
        request = Request(method, parse.path, parse.query, self.headers, self.rfile)
        response = Router.route(request)  # type: Response

        body_as_bytes = bytes(response.body, 'UTF-8')

        self.send_response(response.code)
        self.send_header('Content-type', response.mime_type)
        self.send_header('Content-Length', str(len(body_as_bytes)))
        self.end_headers()
        self.wfile.write(body_as_bytes)


if __name__ == '__main__':
    server_class = HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print(time.asctime(), 'Server Starts - %s:%s' % (HOST_NAME, PORT_NUMBER))
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print(time.asctime(), 'Server Stops - %s:%s' % (HOST_NAME, PORT_NUMBER))
