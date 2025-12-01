from typing import List, Optional
from abc import ABC


class Field(ABC):
    def __init__(self, key: str, value, type: str):
        self.__key = key
        self.__value = value
        self.__type = type

    def get_key(self) -> str:
        return self.__key

    def get_value(self):
        return self.__value

    def get_type(self) -> str:
        return self.__type

    def to_dict(self) -> dict:
        if self.get_type() == "nested_field":
            return {
                "key": self.__key,
                "value": [f.to_dict() for f in self.__value],
                "type": self.__type,
            }
        else:
            return {
                "key": self.__key,
                "value": self.__value,
                "type": self.__type,
            }

    def to_bytes(self) -> bytes:
        import json
        # Преобразуем dict в JSON, затем в байты
        return json.dumps(self.to_dict()).encode("utf-8")

    @staticmethod
    def from_dict(data: dict) -> 'Field':
        f_type = data["type"]
        if f_type == "value_field":
            return ValueField(data["key"], data["value"])
        elif f_type == "comment_field":
            return CommentField(data["value"])
        elif f_type == "nested_field":
            nested = NestedField(data["key"])
            for fdata in data["value"]:
                nested.add_field(Field.from_dict(fdata))
            return nested
        else:
            raise ValueError(f"Unknown field type {f_type}")

    @staticmethod
    def from_bytes(bdata: bytes) -> 'Field':
        import json
        data = json.loads(bdata.decode("utf-8"))
        return Field.from_dict(data)


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

    def to_dict(self) -> dict:
        return {
            "name": self.__name,
            "fields": [f.to_dict() for f in self.__fields],
        }

    def to_bytes(self) -> bytes:
        import json
        return json.dumps(self.to_dict()).encode("utf-8")

    @staticmethod
    def from_dict(data: dict) -> 'Section':
        section = Section(data["name"])
        for fdata in data["fields"]:
            section.add_field(Field.from_dict(fdata))
        return section

    @staticmethod
    def from_bytes(bdata: bytes) -> 'Section':
        import json
        data = json.loads(bdata.decode("utf-8"))
        return Section.from_dict(data)


class Wrapper:
    def __init__(self, sections: Optional[List[Section]] = None) -> None:
        self.__sections = sections if sections is not None else []

    def add_section(self, section: Section) -> None:
        self.__sections.append(section)

    def get_sections(self) -> List[Section]:
        return self.__sections

    def to_dict(self) -> dict:
        return {
            "sections": [s.to_dict() for s in self.__sections]
        }

    def to_bytes(self) -> bytes:
        import json
        return json.dumps(self.to_dict()).encode("utf-8")

    @staticmethod
    def from_dict(data: dict) -> 'Wrapper':
        wrapper = Wrapper()
        for sdata in data["sections"]:
            wrapper.add_section(Section.from_dict(sdata))
        return wrapper

    @staticmethod
    def from_bytes(bdata: bytes) -> 'Wrapper':
        import json
        data = json.loads(bdata.decode("utf-8"))
        return Wrapper.from_dict(data)


class BinarySerializer:
    @staticmethod
    def save_to_file(obj: object, filename: str) -> None:
        with open(filename, 'wb') as f:
            f.write(obj.to_bytes())

    @staticmethod
    def load_from_file(filename: str) -> object:
        with open(filename, 'rb') as f:
            data = f.read()
        return Wrapper.from_bytes(data)
