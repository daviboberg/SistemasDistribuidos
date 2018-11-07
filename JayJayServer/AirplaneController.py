from Response import Response
from AirplaneRepository import AirplaneRepository

class AirplaneController:

    def get(self, request):
        parameters = request.getParameters()
        query = request.getQuery()

        if (len(parameters) == 0 and len(query) == 0):
            return self.__getAll()

        if (len(parameters) == 0):
            return self.__getWithFilters(query)

        return self.__getById(parameters[0])

    def post(self, request):
        parameters = request.getParameters()

        if (len(parameters) == 0):
            return self.__createAirplane(request)

        dict = request.getBodyAsDict()
        json = AirplaneRepository.buy(dict)
        return Response(200, 'application/json', json)


    def __createAirplane(self, request):
        dict = request.getBodyAsDict()
        json = AirplaneRepository.insert(dict)
        return Response(200, 'application/json', json)

    def put(self, request):
        dict = request.getBodyAsDict()
        json = AirplaneRepository.update(dict)
        return Response(200, 'application/json', json)

    def delete(self, request):
        parameters = request.getParameters()
        json = AirplaneRepository.delete(parameters[0])
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
