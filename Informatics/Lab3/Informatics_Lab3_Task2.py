# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 03.11.2025


from re import compile, MULTILINE, fullmatch


class Task2:
	def __init__(self, tests: dict) -> None:
		self.tests = tests


	def check(self, text: str) -> str:
		# Номер моей группы
		my_group = "P3109"
		# Шаблон для студентов с обычными и двойными фамилиями
		surname_pattern = r"[А-Я][а-я]+(?:-[А-Я][а-я]+)?"
		# Шаблон для инициалов, где обе буквы одинаковы
		double_initials_pattern = r"([А-Я])\.\1\."
		# Общий шаблон
		pattern = fr"{surname_pattern} {double_initials_pattern} {my_group}"
		result = ""
		for line in text.strip().split('\n'):
			line = line.strip()
			if not fullmatch(pattern, line):
				result += line + '\n'
		return result.strip()


	def test(self) -> None:
		tests_count = 0		
		correct_tests_count = 0
		for input, output in self.tests.items():
			result = self.check(text=input)
			tests_count += 1
			if result == output:
				correct_tests_count += 1
				print(f"Test №{tests_count} - OK")
			else:
				print(f"Test №{tests_count} - ERROR: Expected output: {output}, your output: {result}")
		print(f"-- {correct_tests_count} of {len(self.tests)} test are correct ! --")


if __name__ == "__main__":
	tests = {
		# Тест 1: строки с моей группой и одинаковыми инициалами
		"""
			Петров П.П. P3109
			Анищенко А.А. P33113
			Примеров Е.В. P3109
			Иванов И.И. P3109
		""": "Анищенко А.А. P33113\nПримеров Е.В. P3109",

		# Тест 2: двойные фамилии
		"""
			Смирнов-Иванов С.С. P3109
			Смирнов-Иванов С.П. P3109
			Иванов И.И. P0001
		""": "Смирнов-Иванов С.П. P3109\nИванов И.И. P0001",

		# Тест 3: двойные инициалы
		"""
			Иванов И.И. P3109
			Петров П.П. P1111
			Кузнецов К.Л. P3109
		""": "Петров П.П. P1111\nКузнецов К.Л. P3109",

		# Тест 4: пустая строка
		"": "",

		# Тест 5: без удаления
		"""
			Алексеев А.Б. P2222
			Борисов Б.В. P3333
		""": "Алексеев А.Б. P2222\nБорисов Б.В. P3333",

		# Тест 6: строки только из удаляемых студентов
		"""
			Иванов И.И. P3109
			Петров П.П. P3109
		""": "",

		# Тест 7: сохранение строки не по формату
		"""
			Ошибка формата
			Петров П.П. P3109
		""": "Ошибка формата",

		# Тест 8: инициалы разные, но группа совпадает - не удаляем
		"""
			Смирнов С.П. P3109
			Петров П.П. P3109
		""": "Смирнов С.П. P3109",

		# Тест 9: инициал один, не совпадает по шаблону - не удаляем
		"""
			Иванова А. P3109
			Петров П.П. P3109
		""": "Иванова А. P3109",

		# Тест 10: одинаковые инициалы с разными группами
		"""
			Иванов И.И. P3109
			Петров П.П. P3109
			Сидоров С.С. P0001
			Кузнецов К.К. P3109
		""": "Сидоров С.С. P0001",
	}






	text = """
Петров П.П. P3109
Анищенко А.А. P33113
Примеров Е.В. P3109
Иванов И.И. P3109
"""
	Task = Task2(tests)
	print(Task.check(text))
	Task.test()