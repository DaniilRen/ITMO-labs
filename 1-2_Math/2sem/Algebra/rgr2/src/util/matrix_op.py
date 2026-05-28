import numpy as np
from fractions import Fraction

class MatrixOperation:
    @staticmethod
    def gauss(A: np.ndarray, full: bool = False) -> Tuple[np.ndarray, int, List[int]]:
        A = np.array([[Fraction(x) for x in row] for row in A])
        m, n = A.shape
        rank = 0
        pivots = []
        
        row = 0
        for col in range(n):
            pivot = None
            for r in range(row, m):
                if A[r, col] != 0:
                    pivot = r
                    break
            if pivot is None:
                continue
            
            if pivot != row:
                A[[row, pivot]] = A[[pivot, row]]
            
            if full:
                pivot_val = A[row, col]
                for c in range(n):
                    A[row, c] = A[row, c] / pivot_val
                
                for r in range(m):
                    if r != row and A[r, col] != 0:
                        factor = A[r, col]
                        for c in range(n):
                            A[r, c] = A[r, c] - factor * A[row, c]
            else:
                for r in range(row + 1, m):
                    if A[r, col] != 0:
                        factor = A[r, col] / A[row, col]
                        for c in range(col, n):
                            A[r, c] = A[r, c] - factor * A[row, c]
            
            rank += 1
            pivots.append(col)
            row += 1
            if row >= m:
                break
        
        return A, rank, pivots
    
    @staticmethod
    def nullspace(A: np.ndarray) -> List[np.ndarray]:
        A = np.array([[Fraction(x) for x in row] for row in A])
        A_reduced, rank, pivots = MatrixOperation.gauss(A, full=True)
        n = A.shape[1]
        free_vars = [c for c in range(n) if c not in pivots]
        
        basis = []
        for free in free_vars:
            v = [Fraction(0)] * n
            v[free] = Fraction(1)
            for i, p in enumerate(pivots):
                if i < len(pivots) and i < A_reduced.shape[0]:
                    v[p] = -A_reduced[i, free]
            vec = np.array(v, dtype=object)
            norm = sum(float(x)**2 for x in vec)**0.5
            if norm > 1e-10:
                vec = np.array([x / Fraction(norm) for x in vec])
                basis.append(vec)
        return basis

    @staticmethod
    def rank(A: np.ndarray) -> int:
        _, rank, _ = MatrixOperation.gauss(A, full=False)
        return rank
    
    @staticmethod
    def to_float_matrix(M: np.ndarray) -> np.ndarray:
        return np.array([[float(x) for x in row] for row in M])

    @staticmethod
    def inverse(M: np.ndarray) -> np.ndarray:
        n = M.shape[0]
        Aug = np.hstack([M.copy(), np.eye(n, dtype=object)])
        
        for i in range(n):
            pivot = i
            while pivot < n and Aug[pivot, i] == 0:
                pivot += 1
            if pivot == n:
                raise ValueError("Матрица вырождена")
            
            if pivot != i:
                Aug[[i, pivot]] = Aug[[pivot, i]]
            
            pivot_val = Aug[i, i]
            for j in range(2*n):
                Aug[i, j] = Aug[i, j] / pivot_val
            
            for j in range(n):
                if j != i and Aug[j, i] != 0:
                    factor = Aug[j, i]
                    for k in range(2*n):
                        Aug[j, k] = Aug[j, k] - factor * Aug[i, k]
        
        return Aug[:, n:]

    @staticmethod
    def divisors(n: int) -> Set[int]:
        if n == 0:
            return {0}
        divs = set()
        for i in range(1, int(n**0.5) + 1):
            if n % i == 0:
                divs.update([i, -i, n//i, -(n//i)])
        return divs