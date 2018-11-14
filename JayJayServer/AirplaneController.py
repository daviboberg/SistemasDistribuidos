from Controller import Controller
from AirplaneRepository import AirplaneRepository


class AirplaneController(Controller):

    def __init__(self, request):
        Controller.__init__(self, request)

    def get(self):
        if self.haveParameters():
            return self.__getById(self.parameters[0])

        if self.haveQueries():
            return self.__getWithFilters(self.query)

        return self.__getAll()

    def post(self):
        print("Post")
        if not self.haveParameters():
            return self.__createAirplane(self.request)

        if self.__isBuyAction():
            return self.__buy()

        return self.createErrorResponse("Action not found!")

    def put(self):
        dict = self.request.getBodyAsDict()
        if AirplaneRepository.update(dict):
            return self.createSuccessResponse("Airplane updated with success!")

        return self.createErrorResponse("Error while updating the airplane!")

    def delete(self):
        if AirplaneRepository.delete(self.parameters[0]):
            return self.createSuccessResponse("Airplane deleted with success!")
        return self.createErrorResponse("Error while deleting the airplane!")

    def __buy(self):
        dict = self.request.getBodyAsDict()
        if AirplaneRepository.buy(dict):
            return self.createSuccessResponse("Ticket bought with success!")

        return self.createErrorResponse("Error while buying ticket for airplane!")

    def __createAirplane(self, request):
        dict = request.getBodyAsDict()
        if AirplaneRepository.insert(dict):
            return self.createSuccessResponse("Airplane created with success!")

        return self.createErrorResponse("Error while creating airplane!")

    def __getAll(self):
        airplanes = AirplaneRepository().getAll()
        return self.createResponseFromList(airplanes)

    def __getWithFilters(self, query):
        airplanes = AirplaneRepository().getWithFilters(query)
        return self.createResponseFromList(airplanes)

    def __getById(self, id):
        airplane = AirplaneRepository().getById(id)
        return self.createResponse(airplane)

    def __isBuyAction(self):
        return self.parameters[0] == "buy"
