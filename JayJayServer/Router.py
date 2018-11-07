import AirplaneController
from Response import Response


class Router:

    @staticmethod
    def route(request):
        try:
            uri_parts = request.path.split('/')
            klass = getattr(AirplaneController, uri_parts[1] + "Controller")
            instance = klass()
            return getattr(instance, request.method)(request)
        except Exception as inst:
            print (inst)
            print (inst.__traceback__)
            return Response(404, "application/json", '{"error": "Page not found!"}')
