# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 20.10.2025


from re import findall


class Task1:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> list:
		pattern = r"\bВТ\b(?:\W+\w+){0,4}\W+\bИТМО\b"
		return findall(pattern, text)

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
		"А ты знал, что ВТ – лучшая кафедра в ИТМО?": ["ВТ – лучшая кафедра в ИТМО"],
		"ВТ ИТМО": ["ВТ ИТМО"],
		"ВТ лучшая ИТМО": ["ВТ лучшая ИТМО"],
		"ВТ лучшая кафедра ИТМО": ["ВТ лучшая кафедра ИТМО"],
		"ВТ здесь точно четыре слова ИТМО": ["ВТ здесь точно четыре слова ИТМО"],
		"ВТ здесь целых пять слов подряд ИТМО": [],
		"Перед ВТ одна фраза ИТМО, а после ВТ второй тест ИТМО": ["ВТ одна фраза ИТМО", "ВТ второй тест ИТМО"],
		"ВТ слово_один, слово_два; слово_три! ИТМО": ["ВТ слово_один, слово_два; слово_три! ИТМО"],
		"ИТМО что-то ВТ что-то": [],
		"ВТ abc_123 789 xyz ИТМО": ["ВТ abc_123 789 xyz ИТМО"],
	}


	text = "А ты знал, что ВТ – лучшая кафедра в ИТМО?"
	Task = Task1(tests)
	print(Task.check(text))
	Task.test()