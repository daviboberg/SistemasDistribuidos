import time
import urllib.parse

from Response import Response
from Request import Request
from Router import Router
from http.server import BaseHTTPRequestHandler, HTTPServer

HOST_NAME = 'localhost'
PORT_NUMBER = 8600


class MyHandler(BaseHTTPRequestHandler):
    # Just receives de get request and passes for processMethod
    def do_GET(self):
        self.processMethod('get')

    # Just receives de post request and passes for processMethod
    def do_POST(self):
        self.processMethod('post')

    # Just receives de put request and passes for processMethod
    def do_PUT(self):
        self.processMethod('put')

    # Just receives de delete request and passes for processMethod
    def do_DELETE(self):
        self.processMethod('delete')

    def processMethod(self, method):
        parse = urllib.parse.urlparse(self.path)
        # Create an object for the request with method(post, get...), path, query, header and the file used to read the body
        request = Request(method, parse.path, parse.query, self.headers, self.rfile)
        # Route the request and get the response
        response = Router.route(request)  # type: Response

        # Read the boyd as bytes
        body_as_bytes = bytes(response.body, 'UTF-8')

        # Set response code
        self.send_response(response.code)
        # Set header with the content type
        self.send_header('Content-type', response.mime_type)
        # Set the content length
        self.send_header('Content-Length', str(len(body_as_bytes)))
        self.end_headers()
        # Write the body and send the response
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
