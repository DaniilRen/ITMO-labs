from lib.advanced_convertor import AdvancedConvertor
from lib.hand_written_convertor import HandWrittenConvertor
from lib.test import TestRunner
from os import path

if __name__ == "__main__":
	INPUT_FILE_SRC = path.abspath(path.join(path.dirname('__file__'), 'input', 'schedule.ini'))

	print(f"Вариант: {501993 % 132}\n------------<")

	ini_file_content = HandWrittenConvertor.read_file(INPUT_FILE_SRC)
	deserialized = HandWrittenConvertor.deserialize(file_content=ini_file_content)
	HandWrittenConvertor.serialize(object=deserialized, format='ron')
	# print(f"Обязательное задание (десериализация):\n{ini_file_content}")

