import AirplaneController
import HotelController
from Response import Response


class Router:

    @staticmethod
    def route(request):
        try:
            uri_parts = request.path.split('/')
            klass = Router.getKlass(uri_parts[1])
            instance = klass(request)
            return getattr(instance, request.method)()
        except Exception as inst:
            print (inst)
            print (inst.__traceback__)
            return Response(404, "application/json", '{"error": "Action not found!"}')

    @staticmethod
    def getKlass(model):
        klass = getattr(AirplaneController, model + "Controller", None)
        if klass is not None:
            return klass
        klass = getattr(HotelController, model + "Controller", None)
        if klass is not None:
            return klass



