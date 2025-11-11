""" 
Конвертор для обязательного задания, дополнительного задания №1 и №3.
Без использования сторонних библиотек осуществляет: 
- десериалиацию данных в формате INI,
- сериализацию в формат RON,
- сериализацию в формат XML
"""

from .convertor import Convertor
from .formal import Wrapper, Section, Field, ValueField, CommentField, NestedField
from typing import Type, List
import os


class HandWrittenConvertor(Convertor):
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
				full_field_key, value = field.split('=')[:2]
				actual_field_key = full_field_key[len(prefix)+1:]
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

				if row[0] == '!' or row[0] == ';':
					section.add_field(CommentField(value=row[1:].strip()))
				
				elif is_nested(row):
					if row.split('.')[0] in [field.get_key() for field in nested_fields]:
						continue
					nested_fields.append(find_subfields(prefix=row.split('.')[0], fields=section_fields))

				else:
					section.add_field(ValueField(*row.split('=')))

		# sections = wrapper.get_sections()
		# for sec in sections:
		# 	print(sec.get_name())
		# 	for field in sec.get_fields():
		# 		print(field.get_key(), [f"key={f.get_key()} value={f.get_value()} type={f.get_type()}" for f in field.get_value()])
		# 	print('-'*40+'<')

		return wrapper


	@staticmethod
	def serialize(object: Wrapper, format: str):
		if format.lower() == 'ron':

			def write_field(out, key, value, type, indent):
				if type == "value_field":
					out.write(f"{indent}{key}: {value}\n")
					return
				if type == "comment_field":
					out.write(f"{indent}//{value}\n")
					return
				out.write(f"{indent}{key}: {{\n")
				for field in value:
					write_field(out, field.get_key(), field.get_value(), field.get_type(), indent*2)
				out.write(f"{indent}}}\n")

			output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_hand_written.ron'))
			with open(output, 'w', encoding='utf-8') as out:
				out.write("Schedule(\n")

				indent = "	"
				for section in object.get_sections():
					out.write(f"{indent}{section.get_name()}: {{\n")

					for field in section.get_fields():
						field_type = field.get_type()
						field_key = field.get_key()
						field_value = field.get_value()
						write_field(out, field_key, field_value, field_type, indent*2)
								
					out.write(f"{indent}}}\n")

				out.write(")")

		elif format.lower() == 'xml':

			def write_field(out, key, value, type, indent):
				if type == "value_field":
					out.write(f"{indent}<{key}>{value}</{key}>\n")
					return
				if type == "comment_field":
					out.write(f"{indent}<!-- {value} -->\n")
					return
				out.write(f"{indent}<{key}>\n")
				for field in value:
					write_field(out, field.get_key(), field.get_value(), field.get_type(), indent*2)
				out.write(f"{indent}</{key}>\n")

			output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_hand_written.xml'))
			with open(output, 'w', encoding='utf-8') as out:
				out.write("""<?xml version="1.0" encoding="UTF-8"?>\n""")
				out.write("<main>\n")

				indent = "	"
				out.write(f"{indent}<Schedule>\n")

				for section in object.get_sections():
					out.write(f"{indent*2}<{section.get_name()}>\n")

					for field in section.get_fields():
						field_type = field.get_type()
						field_key = field.get_key()
						field_value = field.get_value()
						write_field(out, field_key, field_value, field_type, indent*3)
					out.write(f"{indent*2}</{section.get_name()}>\n")

				out.write(f"{indent}</Schedule>\n")
				out.write("</main>\n")