""" 
Классы-обертки для расписания:
удобны для промежуточного представления данных при переводе из одного формата файлов в другой.
"""

from typing import Type, List
from abc import ABC


class Wrapper:
	def __init__(self, sections: List[Section]=list()) -> None:
		self.__sections = sections

	def add_section(self, section: Section) -> None:
		self.__sections.append(section)

	def get_sections(self) -> list[Section]:
		return self.__sections


class Section:
	def __init__(self, name: str, fields: List[Type[Field]]=list()) -> None:
		self.__name = name
		self.__fields = fields

	def add_field(self, field: Field) -> None:
		self.__fields.append(field)

	def get_fields(self) -> list[Field]:
		return self.__fields
	
	def get_name(self) -> str:
		return self.__name
	

class Field(ABC):
	# type can be 'field', 'nested', 'comment'
	def __init__(self, key, value, type):
		self.__key = key
		self.__value = value
		self.__type = type

	def get_key(self) -> dict:
		return self.__key

	def get_value(self) -> dict:
		return self.__value

	def get_type(self) -> str:
		return self.__type


class NestedField(Field):
	def __init__(self, key: str):
		self.__value: List[Type[Field]] = list()
		super().__init__(key, self.__value, "nested_field")

	def add_field(self, field: ValueField) -> None:
		self.__value.append(field)


class ValueField(Field):
	def __init__(self, key: str, value: str):
		super().__init__(key, value, "value_field")
	

class CommentField(Field):
	def __init__(self, value: str):
		super().__init__("#", value, "comment_field")