from lib.advanced_convertor import AdvancedConvertor
from lib.raw_convertor import RawConvertor
from lib.test import TestRunner
from os import path

if __name__ == "__main__":
	INPUT_FILE_SRC = path.abspath(path.join(path.dirname('__file__'), 'input', 'schedule.ini'))

	print(f"Вариант: {501993 % 132}\n------------<")

	ini_file_content = RawConvertor.read_file(INPUT_FILE_SRC)
	deserialized = RawConvertor.deserialize(file_content=ini_file_content)
	# print(f"Обязательное задание (десериализация):\n{ini_file_content}")

