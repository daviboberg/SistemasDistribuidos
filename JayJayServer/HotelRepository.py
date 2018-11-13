from DatabaseConnection import DBConnection
from Hotel import Hotel


class HotelRepository:

    @staticmethod
    def getAll():
        sql = """
            SELECT h.name, h.city, count(hr.id) as rooms
            FROM hotel h
                        JOIN hotel_room hr ON h.id = hr.hotel_id
            GROUP BY h.id;
        """
        cursor = DBConnection.cursor()

        cursor.execute(sql)
        hotels = HotelRepository.__createHotelListFromCursor(cursor)

        return hotels

    @staticmethod
    def __createHotelListFromCursor(cursor):
        hotels = []
        for data in cursor.fetchall():
            hotels.append(HotelRepository.__createHotelFromData(data))
        return hotels

    @staticmethod
    def __createHotelFromData(data):
        airplane = Hotel(data[0], data[1], data[2])
        return airplane