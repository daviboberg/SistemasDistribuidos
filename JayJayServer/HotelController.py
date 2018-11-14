from HotelRepository import HotelRepository
from Controller import Controller


class HotelController(Controller):

	def __init__(self, request):
		super().__init__(request)

	def get(self):
		if self.haveParameters():
			return self.__getById()

		print(self.query)
		if self.haveQueries():
			return self.__getWithFilters(self.query)

		return self.__getAll()

	def post(self):
		return self.__createHotel();

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

	def __getWithFilters(self, query):
		hotels = HotelRepository().getWithFilters(query)
		response = self.createResponseFromList(hotels)
		print(response.body)
		print(response.mime_type)
		return response
