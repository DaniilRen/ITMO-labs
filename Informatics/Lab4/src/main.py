from lib.advanced_convertor import AdvancedConvertor
from lib.hand_written_convertor import HandWrittenConvertor
from lib.formal import BinarySerializer, Wrapper
from lib.test import TestRunner
from os import path


if __name__ == "__main__":
    INPUT_FILE_SRC = path.abspath(path.join(path.dirname('__file__'), 'input', 'schedule.ini'))
    BIN_FILE_PATH = path.abspath(path.join(path.dirname('__file__'), 'output', 'schedule_hand_written.bin'))

    print(f"Вариант: {501993 % 132}")

    # Обязательное задание: десериализация в бинарный объект
    ini_file_content = HandWrittenConvertor.read_file(INPUT_FILE_SRC)
    deserialized_raw = HandWrittenConvertor.deserialize(file_content=ini_file_content)
    BinarySerializer.save_to_file(deserialized_raw, BIN_FILE_PATH)

    # Дополнительное задание 1: загрузка из бинарного + сериализация в RON
    loaded_obj = BinarySerializer.load_from_file(BIN_FILE_PATH)
    HandWrittenConvertor.serialize(obj=loaded_obj, format='ron')

    # Дополнительное задание 2: парсинг через библиотеки
    ini_parser = AdvancedConvertor.read_file(INPUT_FILE_SRC)
    deserialized_advanced = AdvancedConvertor.deserialize(parse_object=ini_parser)
    AdvancedConvertor.serialize(deserialized_advanced, format='ron')

    # Дополнительное задание 3: загрузка из бинарного + сериализация в XML
    loaded_obj_xml = BinarySerializer.load_from_file(BIN_FILE_PATH)
    HandWrittenConvertor.serialize(obj=loaded_obj_xml, format='xml')

    # Дополнительное задание 4: 100 итераций тестов
    TestRunner.run_handwritten_test(INPUT_FILE_SRC, BIN_FILE_PATH, 100)
    TestRunner.run_advanced_test(INPUT_FILE_SRC, 100)
