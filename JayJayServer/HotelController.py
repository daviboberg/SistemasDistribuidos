from HotelRepository import HotelRepository
from Controller import Controller


class HotelController(Controller):

    def __init__(self, request):
        super().__init__(request)

    def get(self):
        return self.__getAll()

    def __getAll(self):
        hotels = HotelRepository.getAll()
        return self.createResponseFromList(hotels)

