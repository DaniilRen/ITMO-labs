from abc import ABC, abstractmethod


class Convertor(ABC):
	@abstractmethod
	def read_file():
		pass
		
	@abstractmethod
	def deserialize():
		pass

	@abstractmethod
	def serialize():
		pass
