import numpy as np

class TestCarrier:
    def __init__(self, mat: np.ndarray, name: str):
        self._name = name
        self._mat = mat

    def get_name(self) -> str:
        return self._name

    def get_matrix(self) -> np.ndarray:
        return self._mat