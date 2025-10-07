# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 20.10.2025


from re import compile, MULTILINE


class Task2:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> str:
		my_group = "P0000"
		# Шаблон для фамилии с возможным дефисом: одна или две части с заглавной буквы и строчные буквы
		surname_pattern = r"[А-Я][а-я]+(?:-[А-Я][а-я]+)?"
		# Шаблон для инициалов, где обе буквы одинаковы
		initials_pattern = r"([А-Я])\.\1\."
		pattern = compile(fr"^{surname_pattern} {initials_pattern} {my_group}$", MULTILINE)
		result_lines = []
		for line in text.strip().split('\n'):
			if not pattern.match(line):
					result_lines.append(line)

		return "\n".join(result_lines)


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
		# Тест 1: все строки разные, только некоторые удалятся
		"""
		Иванов И.И. P0000
		Петров П.П. P0000
		Сидоров С.С. P0001
		Смирнов С.С. P0000
		""": "Сидоров С.С. P0001",

		# Тест 2: фамилия с дефисом, одинаковые инициалы и группа P0000
		"""
		Смирнов-Иванов С.С. P0000
		Смирнов-Иванов С.П. P0000
		""": "Смирнов-Иванов С.П. P0000",

		# Тест 3: разные инициалы, группа P0000
		"""
		Петров П.П. P0000
		Кузнецов К.Л. P0000
		""": "Кузнецов К.Л. P0000",

		# Тест 4: группа не P0000, инициалы одинаковые
		"""
		Иванов И.И. P1111
		Петров П.П. P0000
		""": "Иванов И.И. P1111",

		# Тест 5: пустая строка
		"": "",

		# Тест 6: строка не по формату
		"""
		Ошибка формата
		Петров П.П. P0000
		""": "Ошибка формата",

		# Тест 7: все строки должны остаться
		"""
		Алексеев А.Б. P1234
		Борисов Б.В. P5678
		""": "Алексеев А.Б. P1234\nБорисов Б.В. P5678",

		# Тест 8: все строки должны удалиться
		"""
		Иванов И.И. P0000
		Петров П.П. P0000
		""": "",

		# Тест 9: три части, одна строка удаляется, другая остаётся
		"""
		Петров-Петров П.П. P0000
		Петров-Петров П.Р. P0000
		""": "Петров-Петров П.Р. P0000",

		# Тест 10: смешанный кейс сложный
		"""
		Петров П.П. P0000
		Иванова И.И. P0001
		Смирнов-Иванов С.С. P0000
		Васильев В.В. P9999
		""": "Иванова И.И. P0001\nВасильев В.В. P9999",
	}





	text = """
Петров П.П. P0000
Анищенко А.А. P33113
Примеров Е.В. P0000
Иванов И.И. P0000
"""
	Task = Task2(tests)
	print(Task.check(text))
	Task.test()