from .test_carrier import TestCarrier
from JNF import JNFSolver
from util import MatrixOperation
import numpy as np


class TestService:
    tests = list[TestCarrier]

    def __init__(self) -> None:
        self.tests = []

    def add_test(self, test: TestCarrier) -> None:
        self.tests.append(test)

    def run_tests(self) -> None:
        passed = 0
        for i, test in enumerate(self.tests):
            try:
                jnf_solver = JNFSolver(test.get_matrix())
                print("Исходная матрица:")
                print(test.get_matrix())
                print()
                J, P = jnf_solver.compute()
                print("ЖНФ:")
                print(J)
                print()
                print("Матрица перехода")
                print(P)
                print()
                if jnf_solver.verify():
                    passed += 1
                    print(f"Тест {i}: {test.get_name()} - OK")
                else:
                    print(f"Тест {i}: {test.get_name()} - FAIL")
            except Exception as e:
                print(f"Тест {i}: {test.get_name()} - ERROR: {e}")
        
        print(f"\nРезультат: {passed}/{len(self.tests)}")


