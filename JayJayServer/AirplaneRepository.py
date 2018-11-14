from datetime import datetime

import JsonHelper
from Airplane import Airplane
from DatabaseConnection import DBConnection
import json


class AirplaneRepository:

    @staticmethod
    def getAll():
        sql = "SELECT * FROM airplane;"
        cursor = DBConnection.cursor()

        cursor.execute(sql)
        airplanes = AirplaneRepository.__createAirplaneListFromCursor(cursor)

        return airplanes

    @staticmethod
    def getById(id):
        sql = "SELECT * FROM airplane WHERE id = :id;"
        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        data = cursor.fetchone()

        if data is None:
            return None

        airplane = AirplaneRepository.__createAirplaneFromData(data)

        return airplane

    @staticmethod
    def getWithFilters(query):
        sql = "SELECT * FROM airplane WHERE origin = :origin AND destination = :destination AND date(`date`) = :date;"
        cursor = DBConnection.cursor()

        cursor.execute(sql, {
            "origin": query['origin'][0],
            "destination": query['destination'][0],
            "date": query['date'][0]
        })

        airplanes = AirplaneRepository.__createAirplaneListFromCursor(cursor)
        return airplanes

    @staticmethod
    def insert(dict):
        sql = "INSERT INTO airplane (`flight_number`, `origin`, `destination`, `seats`, `price`, `date`) VALUES (:flight_number, :origin, :destination, :seats, :price, :date);"

        cursor = DBConnection.cursor()

        cursor.execute(sql, dict)
        DBConnection.commit()

        if cursor.rowcount == 0:
            return False

        return True

    @staticmethod
    def update(dict):
        sql = "UPDATE airplane SET `flight_number` = :flight_number, `origin` = :origin, `destination` = :destination, `seats` = :seats, `price` = :price, `date` = :date WHERE id = :id;"

        cursor = DBConnection.cursor()

        cursor.execute(sql, dict)
        DBConnection.commit()

        if cursor.rowcount == 0:
            return False

        return True

    @staticmethod
    def delete(id):
        sql = "DELETE FROM airplane WHERE id = :id"

        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        DBConnection.commit()

        if cursor.rowcount == 0:
            return False

        return True

    @staticmethod
    def buy(dict):
        id = dict['id']
        available_seats = AirplaneRepository.availableSeats(id)
        desired_number_of_seats = int(dict['number_of_seats'])

        print("Buying")
        if available_seats - desired_number_of_seats < 0:
            return False

        sql = "INSERT INTO airplane_ticket (`airplane_id`) VALUES (:airplane_id);"

        cursor = DBConnection.cursor()

        for n in range(desired_number_of_seats):
            cursor.execute(sql, {"airplane_id": id})
            if cursor.rowcount == 0:
                return False

        DBConnection.commit()
        print("Bought")
        return True

    @staticmethod
    def availableSeats(id):
        sql = """
SELECT a.seats - COALESCE(count(at.id), 0) available_seats
FROM airplane a
       LEFT JOIN airplane_ticket at ON a.id = at.airplane_id
WHERE a.id = :id;"""
        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        data = cursor.fetchone()
        print(data)

        return data[0]

    @staticmethod
    def __createAirplaneListFromCursor(cursor):
        airplanes = []
        for data in cursor.fetchall():
            airplanes.append(AirplaneRepository.__createAirplaneFromData(data))
        return airplanes

    @staticmethod
    def __createAirplaneFromData(data):
        available_seats = AirplaneRepository.availableSeats(data[0])
        airplane = Airplane(data[0], data[1], data[2], data[3], data[4], data[5], data[6], available_seats)
        return airplane

