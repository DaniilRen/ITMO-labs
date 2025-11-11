from lib.advanced_convertor import AdvancedConvertor
from lib.hand_written_convertor import HandWrittenConvertor
from lib.test import TestRunner
from os import path

if __name__ == "__main__":
	INPUT_FILE_SRC = path.abspath(path.join(path.dirname('__file__'), 'input', 'schedule.ini'))

	print(f"Вариант: {501993 % 132}\n------------<")

	ini_file_content = HandWrittenConvertor.read_file(INPUT_FILE_SRC)
	# Обязательное задание
	deserialized = HandWrittenConvertor.deserialize(file_content=ini_file_content)
	# Дополнительное задание 1
	HandWrittenConvertor.serialize(object=deserialized, format='ron')
	# Дополнительное задание 2
	
	# Дополнительное задание 3
	HandWrittenConvertor.serialize(object=deserialized, format='xml')
	# Дополнительное задание 4
	

