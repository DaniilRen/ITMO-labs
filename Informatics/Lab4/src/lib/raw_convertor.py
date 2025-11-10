""" 
Конвертор для обязательного задания, дополнительного задания №1 и №3.
Без использования сторонних библиотек осуществляет: 
- десериалиацию данных в формате INI ,
- сериализацию в формат RON,
- сериализацию в формат XML
"""

from .convertor import Convertor
from .formal import Wrapper, Section, ValueField, CommentField, NestedField
from typing import List
import os


class RawConvertor(Convertor):
	@staticmethod
	def read_file(src: os.path):
		with open(src, 'r', encoding='UTF-8') as f:
			return list(filter(lambda row: row != "", map(lambda row: row.strip(), f.readlines())))
		
	@staticmethod
	def deserialize(file_content: List[str]):
		
		def is_section(row):
			return row.startswith('[') and row.endswith(']')
		
		def is_nested(row):
			key = row.split('.')[0]
			return not is_section(row) and row.find(key)+len(key) < row.find('=')
		
		wrapper = Wrapper()
		section_opened = False
		nested_fields = list()

		for i in range(len(file_content)):
			row = file_content[i]


			if is_section(row) or i == len(file_content) - 1:
				if section_opened:
					for nested in nested_fields:
						section.add_field(nested)
					nested_fields = list()
					wrapper.add_section(section)
					if i == len(file_content) - 1:
						break

				section_opened = True
				section_name = row[1:-1]
				section = Section(name=section_name)

			elif row[0] == '#' or row[0] == ';':
				section.add_field(CommentField(value=row[1:].strip()))
			
			elif is_nested(row):
				_key = row.split('.')[0]
				if _key in [field.get_key() for field in nested_fields]:
					continue
				nested = NestedField(key=_key)
				j = i
				while j < len(file_content):
					next_row = file_content[j]
					if not is_nested(row):
						continue

					splited = next_row.split('.')
					if _key == splited[0]:
						nested.add_field(ValueField(*splited[1].split('=')))
					j += 1

				nested_fields.append(nested)

			else:
				section.add_field(ValueField(*row.split('=')))
		
		sections = wrapper.get_sections()
		for sec in sections:
			print('-'*40+'<')
			print(sec.get_name())
			print('-'*40+'>')
			for field in sec.get_fields():
				print(field.get_key(), [f.get_key()+', '+f.get_value() for f in field.get_value()])




	@staticmethod
	def serialize(serialized_object, format: str):
		...
		