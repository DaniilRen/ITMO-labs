from JNF.JNF_service import JNFService
from test.test import TestService
from util.matrix_op import MatrixOperation


def main():
    JNF_service = JNFService()
    test_service = TestService(JNF_service)
    mat = test_service.get_diagonal().get_mat()

    

    
    # print("BEFORE:\n")
    # print(mat, type(mat))
    # mat, pivots = MatrixOperation.gaussian_elimination(mat)
    # print("\nAFTER Gauss:\n")
    # print(mat)
    # print(pivots)


if __name__ == "__main__":
    main()