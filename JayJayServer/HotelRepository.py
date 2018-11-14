from DatabaseConnection import DBConnection
from Hotel import Hotel
from Room import Room


class HotelRepository:

	@staticmethod
	def getAll():
		sql = """
            SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) AS rooms
            FROM hotel h
            LEFT JOIN hotel_room hr ON h.id = hr.hotel_id
            GROUP BY h.id;
        """
		cursor = DBConnection.cursor()

		cursor.execute(sql)
		hotels = HotelRepository.__createHotelListFromCursor(cursor)

		return hotels

	@staticmethod
	def getById(id):
		sql = """
               SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) AS rooms
               FROM hotel h
               LEFT JOIN hotel_room hr ON h.id = hr.hotel_id
               WHERE h.id = :id;
           """
		cursor = DBConnection.cursor()
		cursor.execute(sql, {"id": id})

		data = cursor.fetchone()
		if data is None:
			return None

		hotel = HotelRepository.__createHotelFromData(data)
		return hotel

	@staticmethod
	def insert(dict):
		sql = "INSERT INTO hotel (`name`, `city`) VALUES (:name, :city);"

		cursor = DBConnection.cursor()

		cursor.execute(sql, dict)
		DBConnection.commit()

		if cursor.rowcount == 0:
			return False

		return True

	@staticmethod
	def __createHotelListFromCursor(cursor):
		hotels = []
		for data in cursor.fetchall():
			hotels.append(HotelRepository.__createHotelFromData(data))
		return hotels

	@staticmethod
	def __createHotelFromData(data):
		hotel = Hotel(data[0], data[1], data[2], data[3])
		return hotel

	def getWithFilters(self, query):
		sql = """SELECT *
FROM hotel h
     JOIN hotel_room hr ON h.id = hr.hotel_id
     LEFT JOIN hotel_room_check_in hrc ON hrc.hotel_room_id = hr.id
WHERE hrc.id IS NULL
   OR NOT (
          :desired_out_date BETWEEN hrc.check_in_date AND  hrc.check_out_date
            OR :desired_in_date BETWEEN hrc.check_in_date AND hrc.check_out_date
            OR (
              :desired_out_date > hrc.check_out_date AND :desired_in_date < hrc.check_in_date
              )
          );"""
		cursor = DBConnection.cursor()

		cursor.execute(sql, {
			"check_out_date": query['check_out_date'][0],
			"check_in_date": query['check_in_date'][0],
		})

		rooms = HotelRepository.__createRoomListFromCursor(cursor)
		print(rooms)
		return rooms

	@staticmethod
	def __createRoomListFromCursor(cursor):
		rooms = []
		for data in cursor.fetchall():
			rooms.append(HotelRepository.__createRoomFromData(data))
		return rooms

	@staticmethod
	def __createRoomFromData(data):
		room = Room(data[0], data[1], data[2], data[3])
		return room
