from DatabaseConnection import DBConnection
from Hotel import Hotel


class HotelRepository:

    @staticmethod
    def getAll():
        sql = """
            SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) as rooms
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
               SELECT h.id, h.name, h.city, coalesce (count(hr.id), 0) as rooms
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
        airplane = Hotel(data[0], data[1], data[2], data[3])
        return airplane