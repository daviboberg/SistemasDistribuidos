from Response import Response


class Airplane:
    def __init__(self):
        pass

    def get(self, request):
        response = Response(200, 'application/json', '{"message": "ola"}')
        return response
