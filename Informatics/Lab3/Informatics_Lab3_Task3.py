# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 20.10.2025


from re import compile, match, fullmatch


class Task3:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> bool:
		base_pattern = r'(\*|(\d{1,2})(-\d{1,2})?|(\*(\/)?\d{1,2}))'
		pattern = compile(rf"^{base_pattern}\s{base_pattern}\s{base_pattern}\s{base_pattern}\s{base_pattern}$")
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
		# Valid cron expressions - correct format of fields
		"30 14 * * *": True,
		"*/5 * * * *": True,
		"0 0 1 1 *": True,
		"1-5 0 * * *": True,
		"0-59 0-23 1-31 1-12 0-6": True,

		# Invalid - wrong number of fields
		"30 14 * *": False,
		"*/5 * *": False,
		"0 0 1 1 * *": False,

		# Invalid - illegal characters in fields
		"30 14 * * #": False,
		"*/5 * * ! *": False,
		"0 0 abc 1 *": False,

		# Invalid - missing spaces
		"30 14** * *": False,
		"*/5* * * *": False,
	}



	Task = Task3(tests)
	Task.test()