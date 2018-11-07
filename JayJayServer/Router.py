import Airplane
from Response import Response


class Router:

    @staticmethod
    def route(request):
        try:
            uri_parts = request.uri.split('/')
            klass = getattr(Airplane, uri_parts[1])
            print(klass)
            instance = klass()
            return getattr(instance, request.method)(request)
        except:
            return Response(404, "application/json", '{"error": "Page not found!"}')
