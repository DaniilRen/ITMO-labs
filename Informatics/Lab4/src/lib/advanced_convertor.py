""" 
Конвертор для дополнительного задания №2.
С использованием сторонних библиотек осуществляет:
- десериалиацию данных в формате INI ,
- сериализацию в формат RON
"""

from .convertor import Convertor


class AdvancedConvertor(Convertor):
	@staticmethod
	def deserialize(serialized_object):
		...
	@staticmethod
	def serialize(object, format: str):
		...

	@staticmethod
	def read_file(src):
		with open(src, 'r', encoding='UTF-8') as f:
			return f.read()
