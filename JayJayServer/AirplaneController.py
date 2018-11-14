from Controller import Controller
from AirplaneRepository import AirplaneRepository


class AirplaneController(Controller):

    def __init__(self, request):
        Controller.__init__(self, request)

    def get(self):
        # If there is an parameter, then it is an id and we need to get a specific airplane
        if self.haveParameters():
            return self.__getById(self.parameters[0])
        # If there are queries, we need to get using filters
        if self.haveQueries():
            return self.__getWithFilters(self.query)
        # Return all airplanes
        return self.__getAll()

    def post(self):
        # If don't have parameters, we are creating a new airplane from the post body
        if not self.haveParameters():
            return self.__createAirplane(self.request)
        # If is buy action "Airplane/buy", call buy
        if self.__isBuyAction():
            return self.__buy()
        # Unknown action
        return self.createErrorResponse("Action not found!")

    def put(self):
        # Update an airplane from the put body
        dict = self.request.getBodyAsDict()
        if AirplaneRepository.update(dict):
            return self.createSuccessResponse("Airplane updated with success!")

        return self.createErrorResponse("Error while updating the airplane!")

    def delete(self):
        # Delete airplane of given id from path
        if AirplaneRepository.delete(self.parameters[0]):
            return self.createSuccessResponse("Airplane deleted with success!")
        return self.createErrorResponse("Error while deleting the airplane!")

    def __buy(self):
        # Get a dictionary from the json body
        dict = self.request.getBodyAsDict()
        # Try to buy tickets
        if AirplaneRepository.buy(dict, self.parameters[1]):
            return self.createSuccessResponse("Ticket bought with success!")

        return self.createErrorResponse("Error while buying ticket for airplane!")

    def __createAirplane(self, request):
        # Get a dictionary from the json body
        dict = request.getBodyAsDict()
        # Try to insert a new airplane
        if AirplaneRepository.insert(dict):
            return self.createSuccessResponse("Airplane created with success!")

        return self.createErrorResponse("Error while creating airplane!")

    def __getAll(self):
        # Get all airplanes
        airplanes = AirplaneRepository().getAll()
        return self.createResponseFromList(airplanes)

    def __getWithFilters(self, query):
        # Get airplanes using filters
        airplanes = AirplaneRepository().getWithFilters(query)
        return self.createResponseFromList(airplanes)

    def __getById(self, id):
        # Get a specific airplane from id
        airplane = AirplaneRepository().getById(id)
        return self.createResponse(airplane)

    def __isBuyAction(self):
        return self.parameters[0] == "buy"
