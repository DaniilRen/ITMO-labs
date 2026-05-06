""" Тесты для дополнительного задания №4 """

import time

from lib.advanced_convertor import AdvancedConvertor
from lib.hand_written_convertor import HandWrittenConvertor
from lib.formal import BinarySerializer
from os import path


class TestRunner:
	@staticmethod
	def run_handwritten_test(src: path, bin_src: path, iter_num: int=100,):
		start_time = time.time()

		for _ in range(iter_num):
			ini_file_content = HandWrittenConvertor.read_file(src)
			deserialized_raw = HandWrittenConvertor.deserialize(file_content=ini_file_content)
			BinarySerializer.save_to_file(deserialized_raw, bin_src)
			loaded_obj = BinarySerializer.load_from_file(bin_src)
			HandWrittenConvertor.serialize(obj=loaded_obj, format='ron')

		delta_time = (time.time() - start_time) * 1000
		TestRunner.print_statistics(delta_time, "Собственный парсер")


	@staticmethod
	def run_advanced_test(src: path, iter_num: int=100):
		start_time = time.time()

		for _ in range(iter_num):
			ini_parser = AdvancedConvertor.read_file(src)
			deserialized_advanced = AdvancedConvertor.deserialize(parse_object=ini_parser)
			AdvancedConvertor.serialize(deserialized_advanced, format='ron')

		delta_time = (time.time() - start_time) * 1000
		TestRunner.print_statistics(delta_time, "С использованием библиотек")

	@staticmethod
	def print_statistics(delta_time, name):
		print('='*40)
		print(f"Результат 100 итераций - {name}")
		print(f"Затрачено времени {delta_time} мс, {delta_time/100} мс на 1 итерацию")
