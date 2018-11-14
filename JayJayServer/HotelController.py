from HotelRepository import HotelRepository
from Controller import Controller


class HotelController(Controller):

	def __init__(self, request):
		super().__init__(request)

	def get(self):
		# If there are parameters, the it is an id and we need to get a specific hotel
		if self.haveParameters():
			return self.__getById()
		# If there are queries, we need to get with filters
		if self.haveQueries():
			return self.__getWithFilters(self.query)
		# Return all hotels
		return self.__getAll()

	def post(self):
		# If there are not any parameter, then it is a new hotel insert
		if not self.haveParameters:
			return self.__createHotel()
		# If is an room insertion action "Hotel/insertRoom", then wee need to insert a new room
		if self.__isInsertRoomAction():
			return self.__insertRoom()

		# If is buy action "Hotel/buy", then we need to check_in an hotel room
		if self.__isBuyAction():
			return self.__buy()
			
		# Action not found
		return self.createErrorResponse("Action not found!")

	def __getById(self):
		# Get an specific hotel given an id
		hotel = HotelRepository.getById(self.parameters[0])
		return self.createResponse(hotel)

	def __getAll(self):
		# Get all hotels
		hotels = HotelRepository.getAll()
		return self.createResponseFromList(hotels)

	def __createHotel(self):
		# Get a dictionary from the json body
		dict = self.request.getBodyAsDict()
		# Try to insert  a new hotel
		if HotelRepository.insert(dict):
			return self.createSuccessResponse("Hotel created with success!")

		return self.createErrorResponse("Error while creating hotel!")

	def __getWithFilters(self, query):
		# Get hotels rooms given specific filters
		hotels = HotelRepository().getWithFilters(query)
		response = self.createResponseFromList(hotels)
		return response

	def __insertRoom(self):
		# Get a dictionary from the json body
		dict = self.request.getBodyAsDict()
		# Try to insert a new hotel room
		if HotelRepository.insertRoom(dict):
			return self.createSuccessResponse("Hotel room created with success!")

		return self.createErrorResponse("Error while creating the hotel room!")


	def __isInsertRoomAction(self):
		return self.parameters[0] == "insertRoom"

	def __isBuyAction(self):
		return self.parameters[0] == "buy"

	def __buy(self):
		# Get a dictionary from the json body
		dict = self.request.getBodyAsDict()
		# Try to buy an hotel room given the json body with check_in info and id by parameter
		if HotelRepository.buy(dict, self.parameters[1]):
			return self.createSuccessResponse("Hotel room bought with success!")

		return self.createErrorResponse("Error buying the hotel room!")
