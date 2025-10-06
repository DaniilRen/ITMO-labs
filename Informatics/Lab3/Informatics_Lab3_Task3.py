# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 20.10.2025


from re import compile, match


class Task3:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> bool:
		cron_regex = compile(
			r'^'  # начало строки
			r'(\*|(\d{1,2})(-\d{1,2})?(\/\d+)?(,\d{1,2}(-\d{1,2})?(\/\d+)?)*)\s'  # Минуты: *, число, диапазон, шаг, список
			r'(\*|(\d{1,2})(-\d{1,2})?(\/\d+)?(,\d{1,2}(-\d{1,2})?(\/\d+)?)*)\s'  # Часы
			r'(\*|(\d{1,2})(-\d{1,2})?(\/\d+)?(,\d{1,2}(-\d{1,2})?(\/\d+)?)*)\s'  # Дни месяца
			r'(\*|(\d{1,2})(-\d{1,2})?(\/\d+)?(,\d{1,2}(-\d{1,2})?(\/\d+)?)*)\s'  # Месяцы
			r'(\*|(\d{1,2})(-\d{1,2})?(\/\d+)?(,\d{1,2}(-\d{1,2})?(\/\d+)?)*)$'  # Дни недели
		)
		return bool(match(cron_regex, text))

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
		"30 14 * * *": True,
		"*/5 * * * *": True,
		"0 0 1 1 *": True,
		"0-10,20-30/2 * * * *": True,
		"* * * * 0,6": True,
		"60 24 32 13 7": True,  # regex не проверяет числовую корректность
		"*/3 0-23/2 1-15 1-12 0-6": True,
		"5 5 5 5 5": True,
		"5-10/5 0 1 1 1": True,
		"* * * *": False,       # меньше полей
		"* * * * * *": False,   # больше полей
		"abc def ghi jkl mno": False,  # неверные символы
	}

	Task = Task3(tests)
	Task.test()