# Author = Bykh Daniil Maksimovich
# Group = P3109
# Date = 20.10.2025


from re import compile, MULTILINE


class Task2:
	def __init__(self, tests: dict) -> None:
		self.tests = tests

	def check(self, text: str) -> str:
		my_group = "P0000"
		surname_pattern = r"[А-Я][а-я]+(?:-[А-Я][а-я]+)?"
		# Шаблон для инициалов, где обе буквы одинаковы
		initials_pattern = r"([А-Я])\.\1\."
		pattern = compile(fr"^{surname_pattern} {initials_pattern} {my_group}$", MULTILINE)
		result = []
		for line in text.strip().split('\n'):
			if not pattern.match(line):
				result.append(line)

		return "\n".join(result)


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
    # Тест 1: удаляются строки с вашей группой и одинаковыми инициалами
"""
Петров П.П. P0000
Анищенко А.А. P33113
Примеров Е.В. P0000
Иванов И.И. P0000
""": "Анищенко А.А. P33113\nПримеров Е.В. P0000",

    # Тест 2: двойные фамилии
"""
Смирнов-Иванов С.С. P0000
Смирнов-Иванов С.П. P0000
Иванов И.И. P0001
""": "Смирнов-Иванов С.П. P0000\nИванов И.И. P0001",

    # Тест 3: разные группы, строки с вашей группой удаляются
"""
Иванов И.И. P0000
Петров П.П. P1111
Кузнецов К.Л. P0000
""": "Петров П.П. P1111\nКузнецов К.Л. P0000",

    # Тест 4: пустая строка
    "": "",

    # Тест 5: строки без совпадений для удаления
"""
Алексеев А.Б. P2222
Борисов Б.В. P3333
""": "Алексеев А.Б. P2222\nБорисов Б.В. P3333",

    # Тест 6: строки только из удаляемых студентов
"""
Иванов И.И. P0000
Петров П.П. P0000
""": "",

    # Тест 7: строка с ошибкой формата - не удаляется
"""
Ошибка формата
Петров П.П. P0000
""": "Ошибка формата",

    # Тест 8: инициалы разные, но группа совпадает - не удаляем
"""
Смирнов С.П. P0000
Петров П.П. P0000
""": "Смирнов С.П. P0000",

    # Тест 9: инициал один, не совпадает по шаблону - не удалится
"""
Иванова А. P0000
Петров П.П. P0000
""": "Иванова А. P0000",

    # Тест 10: смешанные группы и инициалы
"""
Иванов И.И. P0000
Петров П.П. P0000
Сидоров С.С. P0001
Кузнецов К.К. P0000
""": "Сидоров С.С. P0001",
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