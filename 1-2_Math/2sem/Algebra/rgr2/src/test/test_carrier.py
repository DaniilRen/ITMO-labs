import numpy as np

class TestCarrier:
    def __init__(self, name: str, mat: np.ndarray):
        self._name = name
        self._mat = mat

    def get_name(self) -> str:
        return self._name

    def get_mat(self) -> np.ndarray:
        return self._mat