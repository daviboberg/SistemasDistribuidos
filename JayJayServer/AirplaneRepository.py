from datetime import datetime
from threading import Lock

import JsonHelper
from Airplane import Airplane
from DatabaseConnection import DBConnection
import json


airplane_lock = Lock()
class AirplaneRepository:

    @staticmethod
    def getAll():
        # Get all airplanes from table
        sql = "SELECT * FROM airplane;"
        cursor = DBConnection.cursor()

        cursor.execute(sql)
        # Create a list of airplanes from cursor
        airplanes = AirplaneRepository.__createAirplaneListFromCursor(cursor)

        return airplanes

    @staticmethod
    def getById(id):
        # Get an airplane with the id :id
        sql = "SELECT * FROM airplane WHERE id = :id;"
        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        data = cursor.fetchone()

        # None airplane found, return error
        if data is None:
            return None

        # Create an airplane from cursor
        airplane = AirplaneRepository.__createAirplaneFromData(data)

        return airplane

    @staticmethod
    def getWithFilters(query):
        # Get airplanes from filters
        sql = "SELECT * FROM airplane WHERE origin = :origin AND destination = :destination AND date(`date`) = :date;"
        cursor = DBConnection.cursor()

        # Parse arguments to query for filters. Can filter by origin, destination and date
        cursor.execute(sql, {
            "origin": query['origin'][0],
            "destination": query['destination'][0],
            "date": query['date'][0]
        })

        # Create a list of airplanes from cursor
        airplanes = AirplaneRepository.__createAirplaneListFromCursor(cursor)
        return airplanes

    @staticmethod
    def insert(dict):
        # Insert a new airplane
        sql = "INSERT INTO airplane (`flight_number`, `origin`, `destination`, `seats`, `price`, `date`) VALUES (:flight_number, :origin, :destination, :seats, :price, :date);"

        cursor = DBConnection.cursor()
        # The json from body needs to have flight_number, origin, destination, seats, price and date
        cursor.execute(sql, dict)
        DBConnection.commit()

        # Check for errors
        return AirplaneRepository.__checkForErrors(cursor)

    @staticmethod
    def update(dict):
        # Update an existing airplane
        sql = "UPDATE airplane SET `flight_number` = :flight_number, `origin` = :origin, `destination` = :destination, `seats` = :seats, `price` = :price, `date` = :date WHERE id = :id;"

        cursor = DBConnection.cursor()
        # The json from body needs to have flight_number, origin, destination, seats, price, date and ID
        cursor.execute(sql, dict)
        DBConnection.commit()

        # Check for errors
        return AirplaneRepository.__checkForErrors(cursor)

    @staticmethod
    def delete(id):
        # Delete an airplane with ID
        sql = "DELETE FROM airplane WHERE id = :id"

        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        DBConnection.commit()

        # Check for errors
        return AirplaneRepository.__checkForErrors(cursor)

    @staticmethod
    def buy(dict, id):
        # Lock mutex
        airplane_lock.acquire()
        # Get the number of available seats
        available_seats = AirplaneRepository.__availableSeats(id)
        # The desired number of seats to buy, comes from the body dictionary
        desired_number_of_seats = int(dict['number_of_seats'])

        # If we want to buy none seats, then everything is ok
        if desired_number_of_seats == 0:
            airplane_lock.release()
            return True
        # If we want to buy more seats then the number that is available, we return an error
        if available_seats - desired_number_of_seats < 0:
            airplane_lock.release()
            return False

        sql = "INSERT INTO airplane_ticket (`airplane_id`) VALUES (:airplane_id);"

        cursor = DBConnection.cursor()

        # Buy each seat
        for n in range(desired_number_of_seats):
            cursor.execute(sql, {"airplane_id": id})
            if cursor.rowcount == 0:
                airplane_lock.release()
                return False

        DBConnection.commit()
        # Release mutex
        airplane_lock.release()
        return True

    @staticmethod
    def __availableSeats(id):
        # Get the number of available seats from the database
        sql = """
SELECT a.seats - COALESCE(count(at.id), 0) available_seats
FROM airplane a
       LEFT JOIN airplane_ticket at ON a.id = at.airplane_id
WHERE a.id = :id;"""
        cursor = DBConnection.cursor()

        cursor.execute(sql, {"id": id})
        data = cursor.fetchone()

        return data[0]

    @staticmethod
    def __checkForErrors(cursor):
        # Check for errors in insert and update
        if cursor.rowcount == 0:
            return False
        return True

    @staticmethod
    def __createAirplaneListFromCursor(cursor):
        # Got throught the cursor and create an list of airplanes
        airplanes = []
        for data in cursor.fetchall():
            airplanes.append(AirplaneRepository.__createAirplaneFromData(data))
        return airplanes

    @staticmethod
    def __createAirplaneFromData(data):
        # Create an airplane from data coming from the database
        available_seats = AirplaneRepository.__availableSeats(data[0])
        airplane = Airplane(data[0], data[1], data[2], data[3], data[4], data[5], data[6], available_seats)
        return airplane

