from HotelRepository import HotelRepository
from Controller import Controller


class HotelController(Controller):

    def __init__(self, request):
        super().__init__(request)

    def get(self):
        if self.haveParameters():
            return self.__getById()

        return self.__getAll()

    def post(self):
        return self.__createHotel()

    def put(self):
        dict = self.request.getBodyAsDict()
        if HotelRepository.update(dict):
            return self.createSuccessResponse("Hotel updated with success!")

        return self.createErrorResponse("Error while updating the hotel!")

    def delete(self):
        if HotelRepository.delete(self.parameters[0]):
            return self.createSuccessResponse("Hotel was deleted with success!")
        return self.createErrorResponse("Error while deleting hotel!")

    def __getById(self):
        hotel = HotelRepository.getById(self.parameters[0])
        return self.createResponse(hotel)

    def __getAll(self):
        hotels = HotelRepository.getAll()
        return self.createResponseFromList(hotels)

    def __createHotel(self):
        dict = self.request.getBodyAsDict()
        if HotelRepository.insert(dict):
            return self.createSuccessResponse("Hotel created with success!")

        return self.createErrorResponse("Error while creating hotel!")


