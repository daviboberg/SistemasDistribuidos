from Object import Object
from Response import Response
import json
import JsonHelper


class Controller:

    def __init__(self, request):
        self.request = request
        self.parameters = request.getParameters()
        self.query = request.getQuery()

    # Check if there are parameters
    def haveParameters(self):
        return len(self.parameters) != 0

    # Check if there are queries
    def haveQueries(self):
        return len(self.query) != 0

    # Create an error response with a given error message
    def createErrorResponse(self, errorMessage):
        obj = Object()
        obj.error = errorMessage
        return self.createResponse(obj)

    # Create an success response with a given success message
    def createSuccessResponse(self, successMessage):
        obj = Object()
        obj.success = successMessage
        return self.createResponse(obj)

    # Create a json response from a given object
    def createResponse(self, obj):
        if obj is None:
            return Response(200, 'application/json', "{}")
        return Response(200, 'application/json', json.dumps(obj.__dict__))

    # Create a json response from a given list
    def createResponseFromList(self, list):
        elementsDict = JsonHelper.getJsonFromList(list)
        return Response(200, 'application/json', json.dumps(elementsDict))
