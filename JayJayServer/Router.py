import AirplaneController
import HotelController
from Response import Response


class Router:

    @staticmethod
    def route(request):
        try:
            path = request.path.split('/')
            # Get the correct  Class to handle the request
            klass = Router.getKlass(path[1])
            # Create intance passing the request to the constructor
            instance = klass(request)
            # Call the corretc method in the controller class. get(), put(), post(), delete()
            return getattr(instance, request.method)()
        except Exception as inst:
            print (inst)
            print (inst.__traceback__)
            return Response(404, "application/json", '{"error": "Action not found!"}')

    @staticmethod
    def getKlass(model):
        # If exists an 'Model'Controller in the module AirplaneController, this is the class
        klass = getattr(AirplaneController, model + "Controller", None)
        if klass is not None:
            return klass
        # If exists an 'Model'Controller in the module HotelController, this is the class
        klass = getattr(HotelController, model + "Controller", None)
        if klass is not None:
            return klass



