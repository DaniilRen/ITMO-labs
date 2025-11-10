""" 
Конвертор для обязательного задания, дополнительного задания №1 и №3.
Без использования сторонних библиотек осуществляет: 
- десериалиацию данных в формате INI ,
- сериализацию в формат RON,
- сериализацию в формат XML
"""

from .convertor import Convertor
from .formal import Wrapper, Section, Field, ValueField, CommentField, NestedField
from typing import Type, List
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
		
		def find_subfields(prefix: str, fields: list=[]) -> NestedField:
			nested = NestedField(key=prefix)
			fields = list(filter(lambda f: f.split('=')[0].startswith(prefix), fields))
			for field in fields:
				full_field_key = field.split('=')[0]
				actual_field_key = full_field_key[len(prefix)+1:]
				value = field.split('=')[1]
				if not '.' in actual_field_key:
					nested.add_field(ValueField(key=actual_field_key, value=value))
				else:
					nested.add_field(find_subfields(full_field_key, fields))
			return nested

		
		wrapper = Wrapper()

		sections = '\n'.join(file_content).split('[')[1:]
		for section_string in sections:
			nested_fields = list()
			section_fields = list(filter(lambda r: r != '', section_string.split('\n')))
			section = Section(name=section_fields[0][:-1])

			for i in range(1, len(section_fields)):
				row = section_fields[i]
				if i == len(section_fields) - 1:
					for nested in nested_fields:
						section.add_field(nested)
					nested_fields = []
					wrapper.add_section(section)

				if row[0] == '#' or row[0] == ';':
					section.add_field(CommentField(value=row[1:].strip()))
				
				elif is_nested(row):
					if row.split('.')[0] in [field.get_key() for field in nested_fields]:
						continue
					nested_fields.append(find_subfields(prefix=row.split('.')[0], fields=section_fields))

				else:
					section.add_field(ValueField(*row.split('=')))


		sections = wrapper.get_sections()
		# print([sec.get_name()+' --- '+str(len(sec.get_fields()))+' --- '+str(sec.get_fields()) for sec in sections])
		for sec in sections:
			print('-'*40+'<')
			print(sec.get_name())
			print('-'*40+'>')

			for field in sec.get_fields():
				print(field.get_key(), [f.get_key()+'='+f.get_value() for f in field.get_value()])


		return wrapper




	@staticmethod
	def serialize(serialized_object, format: str):
		...
		