import numpy as np
from fractions import Fraction
from typing import List, Tuple, Set, Union
from util.matrix_op import MatrixOperation


class JNFSolver:
    def __init__(self, matrix: List[List[Union[int, float]]]) -> None:
        self.A = np.array([[Fraction(x) for x in row] for row in matrix])
        self.n = self.A.shape[0]
        self.J = None
        self.P = None
    
    def eigenvalues(self) -> List[Union[int, float]]:
        if np.allclose(MatrixOperation.to_float_matrix(np.tril(self.A, -1)), 0):
            return [int(self.A[i, i]) for i in range(self.n)]
        
        if self.n == 2:
            trace = float(self.A[0,0] + self.A[1,1])
            det = float(self.A[0,0] * self.A[1,1] - self.A[0,1] * self.A[1,0])
            a, b, c = 1.0, -trace, det
            d = b*b - 4*a*c
            if d >= -1e-10:
                sqrt_d = np.sqrt(max(0, d))
                lam1 = (-b + sqrt_d)/(2*a)
                lam2 = (-b - sqrt_d)/(2*a)
                roots = []
                for lam in [lam1, lam2]:
                    if abs(lam - round(lam)) < 1e-8:
                        roots.append(int(round(lam)))
                    else:
                        roots.append(lam)
                return roots
        
        char_poly = self.characteristic_polynomial()
        free_term = char_poly[-1]
        
        roots = []
        if free_term == 0:
            roots.append(0)
        
        if free_term != 0:
            a0_abs = abs(int(free_term))
            for d in MatrixOperation.divisors(a0_abs):
                val = 0
                for i, coef in enumerate(char_poly):
                    val += coef * (d ** (len(char_poly) - 1 - i))
                if abs(val) < 1e-10 and d not in roots:
                    roots.append(d)
        
        return roots if roots else [0]
    
    def characteristic_polynomial(self) -> List[float]:
        n = self.n
        A = self.A.copy()
        
        B = [np.eye(n, dtype=object)]
        for i in range(n):
            for j in range(n):
                B[0][i,j] = Fraction(1) if i == j else Fraction(0)
        
        p = [Fraction(1)]
        
        for k in range(1, n + 1):
            Bk = np.zeros((n, n), dtype=object)
            for i in range(n):
                for j in range(n):
                    s = Fraction(0)
                    for t in range(n):
                        s += A[i,t] * B[k-1][t,j]
                    Bk[i,j] = s
            
            trace = Fraction(0)
            for i in range(n):
                trace += Bk[i,i]
            pk = -trace / Fraction(k)
            p.append(pk)
            
            if k < n:
                for i in range(n):
                    for j in range(n):
                        Bk[i,j] = Bk[i,j] + (pk if i == j else Fraction(0))
                B.append(Bk)
        
        return [float(x) for x in p]
    
    def multiplicity(self, lam: Union[int, float]) -> int:
        B = self.A - lam * np.eye(self.n, dtype=object)
        Bk = B.copy()
        prev = 0
        
        for k in range(1, self.n + 1):
            defect = self.n - MatrixOperation.rank(Bk)
            if k > 1 and defect == prev:
                return prev
            prev = defect
            if k < self.n:
                Bk = Bk @ B
        return prev
    
    def jordan_blocks(self, lam: Union[int, float]) -> List[int]:
        B = self.A - lam * np.eye(self.n, dtype=object)
        defects = []
        Bk = np.eye(self.n, dtype=object)
        
        for k in range(1, self.n + 1):
            Bk = Bk @ B
            defects.append(self.n - MatrixOperation.rank(Bk))
        
        d = [0] + defects + [defects[-1]]
        blocks = []
        
        for k in range(1, self.n + 1):
            cnt = int(2*d[k] - d[k-1] - d[k+1])
            if cnt > 0:
                blocks.extend([k] * cnt)
        
        blocks.sort(reverse=True)
        return blocks if blocks else [1] * defects[0] if defects else [1]
    
    def jordan_chains(self, lam: Union[int, float], blocks: List[int]) -> List[np.ndarray]:
        B = self.A - lam * np.eye(self.n, dtype=object)
        max_k = max(blocks) if blocks else 1
        
        kernels = []
        Bk = np.eye(self.n, dtype=object)
        for k in range(1, max_k + 2):
            Bk = Bk @ B
            kernels.append(MatrixOperation.nullspace(Bk))
        
        vectors = []
        used = set()
        
        for k in blocks:
            if k == 1:
                found = False
                for v in kernels[0]:
                    key = tuple(float(x) for x in v)
                    if key not in used:
                        used.add(key)
                        vectors.append(v)
                        found = True
                        break
                if not found:
                    v = np.zeros(self.n, dtype=object)
                    v[len(vectors) % self.n] = Fraction(1)
                    vectors.append(v)
            else:
                high = kernels[k-1] if k-1 < len(kernels) else []
                low = kernels[k-2] if k-2 >= 0 and k-2 < len(kernels) else []
                
                v = None
                for vec in high:
                    key = tuple(float(x) for x in vec)
                    if key in used:
                        continue
                    
                    in_low = False
                    for lv in low:
                        diff = sum(float(vec[i] - lv[i])**2 for i in range(self.n))**0.5
                        if diff < 1e-8:
                            in_low = True
                            break
                    
                    if not in_low:
                        v = vec
                        break
                
                if v is None and high:
                    v = high[0]
                elif v is None:
                    v = np.zeros(self.n, dtype=object)
                    v[len(vectors) % self.n] = Fraction(1)
                
                chain = []
                curr = v.copy()
                for _ in range(k):
                    chain.append(curr.copy())
                    used.add(tuple(float(x) for x in curr))
                    curr = B @ curr
                
                vectors.extend(reversed(chain))
        
        return vectors
    
    def compute(self) -> Tuple[np.ndarray, np.ndarray]:
        eigenvals_raw = self.eigenvalues()
        
        eigenvals = []
        for lam in set(eigenvals_raw):
            if isinstance(lam, (int, float)):
                if abs(lam - round(lam)) < 1e-8:
                    lam = int(round(lam))
            m = self.multiplicity(lam)
            eigenvals.extend([lam] * m)
        
        vectors = []
        J = np.zeros((self.n, self.n))
        col = 0
        
        unique_vals = sorted(set(eigenvals), key=lambda x: (x if isinstance(x, (int, float)) else 0), reverse=True)
        
        for lam in unique_vals:
            blocks = self.jordan_blocks(lam)
            chains = self.jordan_chains(lam, blocks)
            
            for v in chains:
                if len(v) < self.n:
                    v = np.pad(v, (0, self.n - len(v)))
                if col < self.n:
                    vectors.append(v)
            
            for k in blocks:
                if col + k > self.n:
                    k = self.n - col
                for i in range(k):
                    J[col + i, col + i] = lam
                    if i < k - 1:
                        J[col + i, col + i + 1] = 1
                col += k
                if col >= self.n:
                    break
        
        while len(vectors) < self.n:
            v = np.zeros(self.n, dtype=object)
            v[len(vectors)] = Fraction(1)
            vectors.append(v)
        
        P_columns = []
        for v in vectors[:self.n]:
            col_vec = [float(x) if isinstance(x, Fraction) else x for x in v]
            P_columns.append(col_vec)
        
        self.P = np.array(P_columns).T
        self.J = J
        return self.J, self.P
    
    def verify(self, tol: float = 1e-6) -> bool:
        try:
            P_inv = MatrixOperation.inverse(self.P)
            P_float = MatrixOperation.to_float_matrix(self.P)
            J_float = self.J
            P_inv_float = MatrixOperation.to_float_matrix(P_inv)
            recon = P_float @ J_float @ P_inv_float
            A_float = MatrixOperation.to_float_matrix(self.A)
            return np.linalg.norm(A_float - recon) < tol
        except Exception:
            return False