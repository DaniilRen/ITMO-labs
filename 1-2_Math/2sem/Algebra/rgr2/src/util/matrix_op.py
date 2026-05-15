import numpy as np

class MatrixOperation:
    def print_matrix(A, name="Матрица") -> None:
        print(f"\n{name}:")
        for row in A:
            print(row)
            # out = []

            # for x in row:
            #     if isinstance(x, Fraction):
            #         if x.denominator == 1:
            #             out.append(int(x))
            #         else:
            #             out.append(float(x))
            #     else:
            #         out.append(x)

            # print(out)


    def get_identity_matrix(n: int) -> np.ndarray:
        E = np.zeros((n, n), dtype=object)
        for i in range(n):
            E[i][i] = Fraction(1)
        return E


    def power(A: np.ndarray, p: int) -> np.ndarray:
        n = A.shape[0]
        result = identity_matrix(n)

        for _ in range(p):
            result = result @ A

        return result


    def gaussian_elimination(_mat: np.ndarray) -> list[np.ndarray]:
        mat = _mat.copy()
        n, m = mat.shape

        pivot_cols = list()

        row = 0

        for col in range(m):

            pivot = None

            for r in range(row, n):
                if mat[r][col] != 0:
                    pivot = r
                    break

            if pivot is None:
                continue

            '''обмен строк'''
            mat[[row, pivot]] = mat[[pivot, row]]

            '''приведение к строк1'''
            pivot_value = mat[row][col]
            mat[row] = [x / pivot_value for x in mat[row]]

            '''вычитания столбца из всех строк'''
            for r in range(n):
                if r != row and mat[r][col] != 0:

                    factor = mat[r][col]

                    mat[r] = [
                        mat[r][c] - factor * mat[row][c]
                        for c in range(m)
                    ]

            pivot_cols.append(col)

            row += 1

            if row == n:
                break

        return mat, pivot_cols


    def get_rank(A) -> int:
        return len(gaussian_elimination(A)[1])


    def get_kernel(mat: np.ndarray) -> np.ndarray:
        R, pivot_cols = gaussian_elimination(mat)

        n = mat.shape[1]

        free_cols = [j for j in range(n) if j not in pivot_cols]

        basis = []

        for free in free_cols:

            vec = [Fraction(0) for _ in range(n)]

            vec[free] = Fraction(1)

            for i, pivot_col in enumerate(pivot_cols):
                vec[pivot_col] = -R[i][free]

            basis.append(vec)

        return basis


    def get_divisors(n: int) -> set[int]:
        n = abs(n)

        if n == 0:
            return set(0)

        result = set()

        for d in range(1, int(math.sqrt(n)) + 1):
            if n % d == 0:
                result.add(d)
                result.add(-d)
                result.add(n // d)
                result.add(-(n // d))

        return sorted(result)