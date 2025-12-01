""" 
Конвертор для обязательного задания, дополнительного задания №1 и №3.
Без использования сторонних библиотек осуществляет: 
- десериалиацию данных в формате INI,
- сериализацию в формат RON,
- сериализацию в формат XML
"""

import os
from .convertor import Convertor
from typing import Sequence
from .formal import Wrapper, Section, ValueField, CommentField, NestedField
from .formal import BinarySerializer


class HandWrittenConvertor(Convertor):
    @staticmethod
    def read_file(src: os.path):
        with open(src, 'r', encoding='UTF-8') as f:
            return list(filter(lambda row: row != "", map(lambda row: row.strip(), f.readlines())))

    @staticmethod
    def deserialize(file_content: Sequence[str]) -> Wrapper:
        def is_section(row):
            return row.startswith('[') and row.endswith(']')

        def is_nested(row):
            prefix = row.split('.')[0]
            return not is_section(row) and row.find(prefix) + len(prefix) < row.find('=')

        def find_subfields(prefix: str, fields: Sequence, nested_prefix: str = "") -> NestedField:
            if nested_prefix == "":
                nested_prefix = prefix
            nested = NestedField(key=nested_prefix)
            # Фильтруем поля начинающиеся с префикса
            relevant_fields = [f for f in fields if f.split('=')[0].startswith(prefix)]

            # Словарь для группировки по следующему уровню вложенности
            groups = {}
            for field in relevant_fields:
                full_key, value = field.split('=', 1)
                sub_key = full_key[len(prefix) + 1:]
                if '.' not in sub_key:
                    nested.add_field(ValueField(key=sub_key, value=value))
                else:
                    group_key = sub_key.split('.')[0]
                    groups.setdefault(group_key, []).append(field)

            for group_key, group_fields in groups.items():
                nested.add_field(find_subfields(prefix=f"{prefix}.{group_key}", fields=group_fields, nested_prefix=group_key))

            return nested


        wrapper = Wrapper()

        sections = '\n'.join(file_content).split('[')[1:]
        for section_string in sections:
            nested_fields = []
            section_fields = list(filter(lambda r: r != '', section_string.split('\n')))
            section = Section(name=section_fields[0][:-1])
            
            for i in range(1, len(section_fields)):
                row = section_fields[i]

                if i == len(section_fields) - 1:
                    for nested in nested_fields:
                        section.add_field(nested)
                    wrapper.add_section(section)

                if row[0] == '#' or row[0] == ';':
                    section.add_field(CommentField(value=row[1:].strip()))
                elif is_nested(row):
                    prefix = row.split('.')[0]
                    # Проверяем, есть ли уже NestedField с этим ключом
                    existing = next((f for f in nested_fields if f.get_key() == prefix), None)
                    if existing is None:
                        nested_fields.append(find_subfields(prefix=prefix, fields=section_fields))
                else:
                    section.add_field(ValueField(*row.split('=', 1)))

            return wrapper


    @staticmethod
    def serialize(obj: Wrapper, format: str):
        format = format.lower()
        if format == 'ron':
            def write_field(out, key, value, type, indent):
                if type == "value_field":
                    out.write(f"{indent}{key}: {value}\n")
                    return
                if type == "comment_field":
                    out.write(f"{indent}//{value}\n")
                    return
                out.write(f"{indent}{key}: (\n")
                for field in value:
                    write_field(out, field.get_key(), field.get_value(), field.get_type(), indent + "  ")
                out.write(f"{indent})\n")

            output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_hand_written.ron'))
            with open(output, 'w', encoding='utf-8') as out:
                out.write("Schedule(\n")
                indent = "  "
                for section in obj.get_sections():
                    out.write(f"{indent}{section.get_name()}: (\n")
                    for field in section.get_fields():
                        write_field(out, field.get_key(), field.get_value(), field.get_type(), indent + "  ")
                    out.write(f"{indent})\n")
                out.write(")")

        elif format == 'xml':
            def write_field(out, key, value, type, indent):
                if type == "value_field":
                    out.write(f"{indent}<{key}>{value}</{key}>\n")
                    return
                if type == "comment_field":
                    out.write(f"{indent}<!-- {value} -->\n")
                    return
                out.write(f"{indent}<{key}>\n")
                for field in value:
                    write_field(out, field.get_key(), field.get_value(), field.get_type(), indent + "  ")
                out.write(f"{indent}</{key}>\n")

            output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_hand_written.xml'))
            with open(output, 'w', encoding='utf-8') as out:
                out.write("""<?xml version="1.0" encoding="UTF-8"?>\n""")
                out.write("<main>\n")
                indent = "  "
                out.write(f"{indent}<Schedule>\n")
                for section in obj.get_sections():
                    out.write(f"{indent*2}<{section.get_name()}>\n")
                    for field in section.get_fields():
                        write_field(out, field.get_key(), field.get_value(), field.get_type(), indent * 3)
                    out.write(f"{indent*2}</{section.get_name()}>\n")
                out.write(f"{indent}</Schedule>\n")
                out.write("</main>\n")

        else:
            raise ValueError(f"Unknown format: {format}")
