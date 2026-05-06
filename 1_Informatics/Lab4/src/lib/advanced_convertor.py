""" 
Конвертор для дополнительного задания №2.
С использованием сторонних библиотек осуществляет:
- десериалиацию данных в формате INI ,
- сериализацию в формат RON
"""

from .convertor import Convertor
import configparser
import xml.etree.ElementTree as ET
import os


class AdvancedConvertor(Convertor):
	@staticmethod
	def deserialize(parse_object: configparser.ConfigParser) -> dict:
		return {section: dict(parse_object.items(section)) for section in parse_object.sections()}
	

	@staticmethod
	def serialize(object: dict, format: str, indent: int=0, return_value: bool=False):
		if format.lower() == 'ron':
			ron = "{\n"
			for key, value in object.items():
				if isinstance(value, dict):
						ron += " " * (indent + 4) + f"{key}: {AdvancedConvertor.serialize(object=value, format='ron', indent=indent + 4, return_value=True).strip()}\n"
				else:
						ron += " " * (indent + 4) + f"{key}: {repr(value)},\n"
			ron += " " * indent + "}"
			
			if return_value:
				return ron
			output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_advanced.ron'))
			with open(output, "w", encoding='utf-8') as out:
				out.write(ron)

		elif format.lower() == 'xml':
			root_name = 'main'
			root = ET.Element(root_name)
			for section, items in object.items():
				section_elem = ET.SubElement(root, section)
				for key, value in items.items():
						item_elem = ET.SubElement(section_elem, key)
						item_elem.text = value
			if return_value:
				return root
			output = os.path.abspath(os.path.join(os.path.dirname('__file__'), 'output', 'schedule_advanced.xml'))
			tree = ET.ElementTree(root)
			tree.write(output, encoding="utf-8", xml_declaration=True)

	@staticmethod
	def read_file(src: os.path) -> configparser.ConfigParser:
		object = configparser.ConfigParser()
		object.read(src, encoding='utf-8')
		return object
