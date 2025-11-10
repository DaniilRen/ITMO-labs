""" Класс-обертка для хранения данных при перводе между форматами """


class DataWrapper:
	def __init__(self, sections: list=list()) -> None:
		self.__sections = sections