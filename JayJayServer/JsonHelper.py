def getJsonFromList(list):
	# Simply get a json string from a list of objects
	elementsDict = []
	for element in list:
		elementsDict.append(element.__dict__)
	return elementsDict