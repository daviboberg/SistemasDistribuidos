import AirplaneController
from Response import Response


class Router:

    @staticmethod
    def route(request):
        try:
            uri_parts = request.path.split('/')
            klass = getattr(AirplaneController, uri_parts[1] + "Controller")
            instance = klass(request)
            return getattr(instance, request.method)()
        except Exception as inst:
            print (inst)
            print (inst.__traceback__)
            return Response(404, "application/json", '{"error": "Action not found!"}')
