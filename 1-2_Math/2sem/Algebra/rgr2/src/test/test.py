from .test_carrier import TestCarrier
from JNF import JNFService
from util import MatrixOperation
import numpy as np

class TestService:
    def __init__(self, JNF_service: JNFService) -> None:
        self.tests = list[TestCarrier]
        self.JNF_service = JNF_service

    def get_diagonal(self):
        return TestCarrier(
        "Диагональная",
        np.array([
            [4, 1, 0],
            [1, 4, 0],
            [0, 0, 2]
        ]))

    def get_single_block(self):
        return TestCarrier(
        "Одна жорданова клетка",
        np.array([
            [4, -1, 1],
            [2, 1, 1],
            [0, 0, 2]
        ]))

    def get_2_blocks(self):
        return TestCarrier(
        "Две жордановых клетки",
        np.array([
            [5, 2, 0, 0],
            [0, 5, 0, 0],
            [0, 0, 5, 3],
            [0, 0, 0, 5]
        ]))

    def get_nilpotent(self):
        return TestCarrier(
        "Нильпотентная",
        np.array([
            [1, 1, -1],
            [1, 1, -1],
            [1, 1, -1]
        ]))

    def get_mixed(self):
        return TestCarrier(
        "Смешанная",
        np.array([
            [6, 2, 0, 0, 0],
            [0, 6, 1, 0, 0],
            [0, 0, 6, 0, 0],
            [0, 2, 0, 3, 1],
            [1, 0, 0, 0, 3]
        ]))

    def add_test(self, test: TestCarrier) -> None:
        tests.append(test)

    def run_tests(self) -> None:
        for test in tests:
            print(f"-> {test.get_name()}")
            J, P = self.JNF_service.get_JNF()
            MatrixOperation.print_matrix(J, "Jordan form (J)")
            MatrixOperation.print_matrix(P, "Trasition matrix (P)")

