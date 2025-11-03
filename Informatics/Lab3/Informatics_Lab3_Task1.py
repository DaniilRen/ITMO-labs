# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 03.11.2025


from re import findall


class Task1:
	def __init__(self, tests: dict) -> None:
		self.tests = tests


	def check(self, text: str) -> list:
		# Шаблон для не более чем 4 слов
		words_pattern = r'(?:\W+\w+){,4}\W+'
		# Общий шаблон
		pattern = rf"\bВТ\b{words_pattern}\bИТМО\b"

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
		"ВТ – лучшая кафедра в ИТМО? ВТ – лучшая кафедра в ИТМО? ВТ – лучшая кафедра в ИТМО?": ['ВТ – лучшая кафедра в ИТМО', 'ВТ – лучшая кафедра в ИТМО', 'ВТ – лучшая кафедра в ИТМО'],
		"Перед ВТ одна фраза ИТМО, а после ВТ второй тест ИТМО": ["ВТ одна фраза ИТМО", "ВТ второй тест ИТМО"],
		"ВТ слово_один, слово_два; слово_три! ИТМО": ["ВТ слово_один, слово_два; слово_три! ИТМО"],
		"ИТМО что-то ВТ что-то": [],
		"ВТ abc_123 789 xyz ИТМО": ["ВТ abc_123 789 xyz ИТМО"],
	}

	Task = Task1(tests)
	Task.test()