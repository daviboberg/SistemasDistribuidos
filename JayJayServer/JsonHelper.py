def getJsonFromList(list):
	elementsDict = []
	for element in list:
		elementsDict.append(element.__dict__)
	return elementsDict