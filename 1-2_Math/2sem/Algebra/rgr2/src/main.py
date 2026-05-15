from JNF.JNF_service import JNFService
from test.test import TestService


def main():
    JNF_service = JNFService()

    test_service = TestService(JNF_service)
    test_service.add_test(test_service.get_diagonal())
    test_service.add_test(test_service.get_single_block())
    test_service.add_test(test_service.get_2_blocks())
    test_service.add_test(test_service.get_nilpotent())
    test_service.add_test(test_service.get_mixed())
    
    test_service.run_tests()
    
if __name__ == "__main__":
    main()