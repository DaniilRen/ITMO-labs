# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 03.11.2025


from re import compile, match, fullmatch


class Task3:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> bool:
		# Шаблон для числа ли диапазона чисел: 59, 12-33
		number_pattern = r'(\d{1,2})(-\d{1,2})?'
		# Шаблон для полей с повторяющиймся значением: *\5
		cycle_pattern = r'(\*(\/)?\d{1,2})'
		# Шаблон для 1 поля Cron-выражения: 59, 12-33, *\5
		base_pattern = rf'(\*|{number_pattern}|{cycle_pattern})'
		# Общий шаблон для всех 5 полей 
		pattern = rf"{base_pattern}\s{base_pattern}\s{base_pattern}\s{base_pattern}\s{base_pattern}"
		return bool(fullmatch(pattern, text))

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
		# Группа тестов 1: все поля верные
		"30 14 * * *": True,
		"*/5 * * * *": True,
		"0 0 1 1 *": True,
		"1-5 0 * * *": True,
		"0-59 0-23 1-31 1-12 0-6": True,

		# Группа тестов 2: неверное количество полей 
		"30 14 * *": False,
		"*/5 * *": False,
		"0 0 1 1 * *": False,

		# Группа тестов 3: некорректные символы
		"30 14 * * #": False,
		"*/5 * * ! *": False,
		"0 0 abc 1 *": False,

		# Группа тестов 4: отсутствие пробелов между полями
		"30 14** * *": False,
		"*/5* * * *": False,
	}



	Task = Task3(tests)
	Task.test()