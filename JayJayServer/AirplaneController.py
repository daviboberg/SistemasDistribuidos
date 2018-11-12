import json

import JsonHelper
from Object import Object
from Response import Response
from AirplaneRepository import AirplaneRepository

class AirplaneController:

    def __init__(self, request):
        self.request = request
        self.parameters = request.getParameters()
        self.query = request.getQuery()

    def get(self):
        if self.__haveParameters():
            return self.__getById(self.parameters[0])

        if self.__haveQueries():
            return self.__getWithFilters(self.query)

        return self.__getAll()


    def post(self):
        if not self.__haveParameters():
            return self.__createAirplane(self.request)

        if self.__isBuyAction():
            return self.__buy()

        return self.__createErrorResponse("Action not found!")

    def put(self):
        dict = self.request.getBodyAsDict()
        if AirplaneRepository.update(dict):
            return self.__createSuccessResponse("Airplane updated with success!")

        return self.__createErrorResponse("Error while updating the airplane!")

    def delete(self):
        if AirplaneRepository.delete(self.parameters[0]):
            return self.__createSuccessResponse("Airplane deleted with success!")
        return self.__createErrorResponse("Error while deleting the airplane!")

    def __buy(self):
        if self.request.getBodyAsDict():
            return self.__createSuccessResponse("Airplane created with success!")

        return self.__createErrorResponse("Error while creating the airplane!")

    def __createErrorResponse(self, successMessage):
        obj = Object()
        obj.error = successMessage
        return self.__createResponse(obj)

    def __createSuccessResponse(self, successMessage):
        obj = Object()
        obj.success = successMessage
        return self.__createResponse(obj)

    def __haveParameters(self):
        return len(self.parameters) != 0

    def __haveQueries(self):
        return len(self.query) != 0


    def __createAirplane(self, request):
        dict = request.getBodyAsDict()
        json = AirplaneRepository.insert(dict)


    def __getAll(self):
        airplanes = AirplaneRepository().getAll()
        return self.__createResponseFromList(airplanes)


    def __getWithFilters(self, query):
        airplanes = AirplaneRepository().getWithFilters(query)
        return self.__createResponseFromList(airplanes)

    def __getById(self, id):
        airplane = AirplaneRepository().getById(id)
        return self.__createResponse(airplane)

    def __createResponse(self, obj):
        if obj is None:
            return Response(200, 'application/json', "{}")
        return Response(200, 'application/json', json.dumps(obj.__dict__))

    def __isBuyAction(self):
        return self.parameters[0] == "buy"
        pass

    def __createResponseFromList(self, list):
        elementsDict = JsonHelper.getJsonFromList(list)
        return Response(200, 'application/json', json.dumps(elementsDict))



