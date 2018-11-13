from Object import Object
from Response import Response
import json
import JsonHelper


class Controller:

    def __init__(self, request):
        self.request = request
        self.parameters = request.getParameters()
        self.query = request.getQuery()

    def haveParameters(self):
        return len(self.parameters) != 0

    def haveQueries(self):
        return len(self.query) != 0

    def createErrorResponse(self, successMessage):
        obj = Object()
        obj.error = successMessage
        return self.createResponse(obj)

    def createSuccessResponse(self, successMessage):
        obj = Object()
        obj.success = successMessage
        return self.createResponse(obj)

    def createResponse(self, obj):
        if obj is None:
            return Response(200, 'application/json', "{}")
        return Response(200, 'application/json', json.dumps(obj.__dict__))

    def createResponseFromList(self, list):
        elementsDict = JsonHelper.getJsonFromList(list)
        return Response(200, 'application/json', json.dumps(elementsDict))
