from threading import Lock

from DatabaseConnection import DBConnection
from Hotel import Hotel
from Room import Room


hotel_lock = Lock()
class HotelRepository:

	@staticmethod
	def getAll():
		# Get all hotels with the count of rooms
		sql = """
			SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) AS rooms
			FROM hotel h
			LEFT JOIN hotel_room hr ON h.id = hr.hotel_id
			GROUP BY h.id;
		"""
		cursor = DBConnection.cursor()

		cursor.execute(sql)
		# Create a hotel list from cursor
		hotels = HotelRepository.__createHotelListFromCursor(cursor)

		return hotels

	@staticmethod
	def getById(id):
		# Get an specific hotel from id
		sql = """
			   SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) AS rooms
			   FROM hotel h
			   LEFT JOIN hotel_room hr ON h.id = hr.hotel_id
			   WHERE h.id = :id;
		   """
		cursor = DBConnection.cursor()
		cursor.execute(sql, {"id": id})

		data = cursor.fetchone()
		# If data is none, then there is an error
		if data is None:
			return None

		# Create an hotel from cursor
		hotel = HotelRepository.__createHotelFromData(data)
		return hotel

	@staticmethod
	def insert(dict):
		# Insert a new hotel
		sql = "INSERT INTO hotel (`name`, `city`) VALUES (:name, :city);"

		cursor = DBConnection.cursor()

		# The post body needs to have name and city
		cursor.execute(sql, dict)
		DBConnection.commit()

		# Check for success
		return HotelRepository.__checkQuerySuccess(cursor)

	@staticmethod
	def __createHotelListFromCursor(cursor):
		hotels = []
		for data in cursor.fetchall():
			hotels.append(HotelRepository.__createHotelFromData(data))
		return hotels

	def getWithFilters(self, query):
		# Get a hotel list using filters
		sql = """
SELECT DISTINCT hr.id, hr.price, hr.size, h.id
FROM hotel h
	 JOIN hotel_room hr ON h.id = hr.hotel_id
	 LEFT JOIN hotel_room_check_in hrc ON hrc.hotel_room_id = hr.id
WHERE hr.price <= :price
  AND (hrc.id IS NULL
		 OR :check_in_date > hrc.check_out_date
		 OR :check_out_date < hrc.check_in_date)
		 AND h.city = :city
		 AND hr.size >= :size;
"""
		cursor = DBConnection.cursor()

		# Parse the query to be usend in the SQL, possible filters are check_out_date, check_in_date, price, location and room capacity
		cursor.execute(sql, {
			"check_out_date": query['check_out_date'][0],
			"check_in_date": query['check_in_date'][0],
			"price": query['price'][0],
			"city": query['location'][0],
			"size": query['capacity'][0],
		})

		# Create an hotel list from cursor
		rooms = HotelRepository.__createRoomListFromCursor(cursor)
		return rooms

	@staticmethod
	def insertRoom(dict):
		# Insert an new hotel room
		sql = """
		INSERT INTO hotel_room (`hotel_id`, `size`, `price`)
		VALUES (:hotel_id, :size, :price)
		"""
		cursor = DBConnection.cursor()
		DBConnection.commit()
		# The post body needs to have hotel_id, size and price
		cursor.execute(sql, dict)
		DBConnection.commit()

		return HotelRepository.__checkQuerySuccess(cursor)

	@staticmethod
	def buy(dict, room_id):
		# Lock mutex
		hotel_lock.acquire()
		# Check if this room is available in the requested date
		if not HotelRepository.__isRoomAvalible(dict, room_id):
			hotel_lock.release()
			return False

        # Insert new check_in entry
		sql = """
		INSERT INTO hotel_room_check_in (`hotel_room_id`, `check_in_date`, `check_out_date`)
		VALUES (:hotel_room_id, :check_in_date, :check_out_date);
		"""
		cursor = DBConnection.cursor()
		dict['hotel_room_id'] = room_id
        # We need to pass in the post body the check_in_date and check_out_date
		cursor.execute(sql, dict)
		DBConnection.commit()

        # Release hotel lock
		hotel_lock.release()
        # Check errors
		return HotelRepository.__checkQuerySuccess(cursor)

	@staticmethod
	def __createHotelFromData(data):
        # Create a hotel from data
		hotel = Hotel(data[0], data[1], data[2], data[3])
		return hotel

	@staticmethod
	def __checkQuerySuccess(cursor):
        # Check for success for insert and update
		if cursor.rowcount == 0:
			return False
		return True

	@staticmethod
	def __createRoomListFromCursor(cursor):
        # Create a Room list from a cursor
		rooms = []
		for data in cursor.fetchall():
			rooms.append(HotelRepository.__createRoomFromData(data))
		return rooms

	@staticmethod
	def __createRoomFromData(data):
        # Create a Room from data
		room = Room(data[0], data[1], data[2], data[3])
		return room

	@staticmethod
	def __isRoomAvalible(dict, room_id):
		# Query to check if the room is not check_in already in the requested date
		sql = """
		SELECT 1
		FROM hotel_room hr
		JOIN hotel_room_check_in hrc on hr.id = hrc.hotel_room_id
		WHERE hr.id = :hotel_room_id AND
		  (:check_in_date BETWEEN hrc.check_in_date AND hrc.check_out_date OR
		  :check_out_date BETWEEN hrc.check_in_date AND hrc.check_out_date) OR
		  (hrc.check_in_date BETWEEN :check_in_date AND :check_out_date OR
		 hrc.check_out_date BETWEEN :check_in_date AND :check_out_date )
		"""
		cursor = DBConnection.cursor()

		dict['hotel_room_id'] = room_id
		cursor.execute(sql, dict)
		# Get hotel
		data = cursor.fetchone()

		# If None returned, then this hotel was not already booked for the desired date
		if data is None:
			return True
        # Otherwise, this room was already booked!
		return False