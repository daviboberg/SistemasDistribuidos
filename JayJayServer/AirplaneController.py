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
            dict = self.request.getBodyAsDict()
            json = AirplaneRepository.buy(dict)
            return Response(200, 'application/json', json)

        return Response(200, 'application/json', '{"error": "Action not found!"}')



    def put(self):
        dict = self.request.getBodyAsDict()
        json = AirplaneRepository.update(dict)
        return Response(200, 'application/json', json)

    def delete(self):
        parameters = self.request.getParameters()
        json = AirplaneRepository.delete(parameters[0])
        return Response(200, 'application/json', json)

    def __haveParameters(self):
        return len(self.parameters) != 0

    def __haveQueries(self):
        return len(self.query) != 0


    def __createAirplane(self, request):
        dict = request.getBodyAsDict()
        json = AirplaneRepository.insert(dict)
        return Response(200, 'application/json', json)

    def __getAll(self):
        json = AirplaneRepository().getAll()
        return Response(200, 'application/json', json)

    def __getWithFilters(self, query):
        json = AirplaneRepository().getWithFilters(query)
        return Response(200, 'application/json', json)

    def __getById(self, id):
        json = AirplaneRepository().getById(id)
        return Response(200, 'application/json', json)

    def __isBuyAction(self):
        return self.parameters[0] == "buy"
        pass
