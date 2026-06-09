from JNF.JNFSolver import JNFSolver
from test.test_service import TestService
from test.test_carrier import TestCarrier
import numpy as np


def main():
    test_service = TestService()

    test_service.add_test(TestCarrier(np.array([[4, 1], [1, 4]]), "Диагонализуемая 2x2"))
    test_service.add_test(TestCarrier(np.array([[2, 0, 0], [0, 3, 0], [0, 0, 5]]), "Диагональная 3x3"))
    test_service.add_test(TestCarrier(np.array([[0, 1, 0], [0, 0, 1], [0, 0, 0]]), "Нильпотентная 3x3"))
    test_service.add_test(TestCarrier(np.array([[3, 1], [0, 3]]), "Жорданова клетка 2x2"))
    test_service.add_test(TestCarrier(np.array([[2, 1, 0], [0, 2, 0], [0, 0, 3]]), "Клетка 2x2 и 1x1"))
    test_service.add_test(TestCarrier(np.array([[2, 1, 0, 0], [0, 2, 0, 0], [0, 0, 2, 0], [0, 0, 0, 2]]), "Клетка 2x2 + две 1x1"))
    test_service.add_test(TestCarrier(np.array([[2, 1, 0, 0], [0, 2, 0, 0], [0, 0, 2, 1], [0, 0, 0, 2]]), "Две клетки 2x2"))
    test_service.add_test(TestCarrier(np.eye(4), "Единичная 4x4"))
    test_service.add_test(TestCarrier(np.array([[3, 1, 0, 0], [0, 3, 0, 0], [0, 0, 5, 1], [0, 0, 0, 5]]), "Две клетки 2x2 (a=3 и a=5)"))
    test_service.add_test(TestCarrier(np.array([[1, 2, 3], [0, 4, 5], [0, 0, 6]]), "Верхнетреугольная"))
    test_service.add_test(TestCarrier(np.array([[1, 1, 0, 0, 0], [0, 1, 1, 0, 0], [0, 0, 1, 0, 0], [0, 0, 0, 2, 1], [0, 0, 0, 0, 2]]), "Клетки 3x3 (a=1) и 2x2 (a=2)"))
    test_service.add_test(TestCarrier(np.array([[1, 1, 0], [0, 1, 0], [0, 0, 1]]), "Клетка 2x2 и 1x1 (λ=1)"))
    test_service.add_test(TestCarrier(np.array([[0, 1, 0, 0, 0, 0], [0, 0, 1, 0, 0, 0], [0, 0, 0, 0, 0, 0], [0, 0, 0, 2, 1, 0], [0, 0, 0, 0, 2, 0], [0, 0, 0, 0, 0, 3]]), "Клетки 3x3 (a=0), 2x2 (a=2), 1x1 (a=3)"))
    test_service.add_test(TestCarrier(np.array([[0, 2], [1, 0]]), "---"))

    test_service.run_tests()
    
if __name__ == "__main__":
    main()