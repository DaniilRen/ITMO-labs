import pickle
from typing import List, Optional
from abc import ABC


class Field(ABC):
    def __init__(self, key: str, value: str, type: str):
        self.__key = key
        self.__value = value
        self.__type = type

    def get_key(self) -> str:
        return self.__key

    def get_value(self) -> str:
        return self.__value

    def get_type(self) -> str:
        return self.__type

    def to_bytes(self) -> bytes:
        # Сериализация объекта с помощью pickle
        return pickle.dumps({
            "key": self.__key,
            "value": self.__value,
            "type": self.__type,
        })


class ValueField(Field):
    def __init__(self, key: str, value: str):
        super().__init__(key, value, "value_field")


class CommentField(Field):
    def __init__(self, value: str):
        super().__init__("!", value, "comment_field")


class NestedField(Field):
    def __init__(self, key: str):
        self.__value: List[Field] = []
        super().__init__(key, self.__value, "nested_field")

    def add_field(self, field: Field) -> None:
        self.__value.append(field)

    def get_fields(self) -> List[Field]:
        return self.__value

    def to_bytes(self) -> bytes:
        # Сериализовать список вложенных полей
        return pickle.dumps({
            "key": self.get_key(),
            "value": [f.to_bytes() for f in self.__value],
            "type": self.get_type(),
        })


class Section:
    def __init__(self, name: str, fields: Optional[List[Field]] = None) -> None:
        self.__name = name
        self.__fields = fields if fields is not None else []

    def add_field(self, field: Field) -> None:
        self.__fields.append(field)

    def get_fields(self) -> List[Field]:
        return self.__fields

    def get_name(self) -> str:
        return self.__name

    def to_bytes(self) -> bytes:
        # Сериализовать секцию: имя и список полей
        return pickle.dumps({
            "name": self.__name,
            "fields": [f.to_bytes() for f in self.__fields],
        })


class Wrapper:
	def __init__(self, sections: Optional[List[Section]] = None) -> None:
		self.__sections = sections if sections is not None else []

	def add_section(self, section: Section) -> None:
		self.__sections.append(section)

	def get_sections(self) -> List[Section]:
		return self.__sections

	def to_bytes(self) -> bytes:
		return pickle.dumps(self)


class BinarySerializer:
    @staticmethod
    def save_to_file(obj: object, filename: str) -> None:
        with open(filename, 'wb') as f:
            f.write(obj.to_bytes())

    @staticmethod
    def load_from_file(filename: str) -> object:
        with open(filename, 'rb') as f:
            data = f.read()
        return pickle.loads(data)
