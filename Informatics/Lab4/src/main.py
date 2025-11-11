from lib.advanced_convertor import AdvancedConvertor
from lib.hand_written_convertor import HandWrittenConvertor
from lib.test import TestRunner
from os import path

if __name__ == "__main__":
	INPUT_FILE_SRC = path.abspath(path.join(path.dirname('__file__'), 'input', 'schedule.ini'))

	print(f"Вариант: {501993 % 132}\n------------|")

	# Обязательное задание
	ini_file_content = HandWrittenConvertor.read_file(INPUT_FILE_SRC)
	deserialized_raw = HandWrittenConvertor.deserialize(file_content=ini_file_content)
	
	# Дополнительное задание 1
	HandWrittenConvertor.serialize(object=deserialized_raw, format='ron')

	# Дополнительное задание 2
	ini_parser = AdvancedConvertor.read_file(INPUT_FILE_SRC)
	deserialized_advanced = AdvancedConvertor.deserialize(parse_object=ini_parser)
	AdvancedConvertor.serialize(deserialized_advanced, format='ron')
	
	# Дополнительное задание 3
	HandWrittenConvertor.serialize(object=deserialized_raw, format='xml')
	
	# Дополнительное задание 4
	TestRunner.run_handwritten_test(INPUT_FILE_SRC)
	TestRunner.run_advanced_test(INPUT_FILE_SRC)

