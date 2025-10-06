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
		surname_pattern = r"[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?"
		# Шаблон для инициалов, где обе буквы одинаковы
		initials_pattern = r"([А-ЯЁ])\.\1\."
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
		# Обычные фамилии, одинаковые инициалы, группа P0000 (удалить)
		"Петров П.П. P0000": "",
		"Анищенко А.А. P33113": "Анищенко А.А. P33113",
		"Примеров Е.В. P0000": "Примеров Е.В. P0000",
		"Иванов И.И. P0000": "",
		
		# Двойная фамилия, инициалы одинаковые, группа P0000 (удалить)
		"Смирнов-Иванов С.С. P0000": "",
		
		# Двойная фамилия, инициалы разные, группа P0000 (оставить)
		"Смирнов-Иванов С.П. P0000": "Смирнов-Иванов С.П. P0000",
		
		# Инициалы одинаковые, но другая группа (оставить)
		"Петров П.П. P9999": "Петров П.П. P9999",
		
		# Фамилия без дефиса, инициалы одинаковые, группа другая (оставить)
		"Иванов И.И. P3321": "Иванов И.И. P3321",
		
		# Инициалы разные, группа ваша (оставить)
		"Кузнецов К.Л. P0000": "Кузнецов К.Л. P0000",
		
		# Пустая строка (не влияет)
		"": "",
		
		# Ошибочный формат, не удалять
		"НеФормат": "НеФормат",
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