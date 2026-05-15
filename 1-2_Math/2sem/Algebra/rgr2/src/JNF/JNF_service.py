class JNFService:
    def get_JNF():
        n = A.shape()

        print("\nСобственные значения:")
        for lam, mult in get_eigenvalues(A).items():
            print(f"lambda = {lam}, алгебраическая кратность = {mult}")

    # Метод Фадеева-Леверье
    def characteristic_polynomial(A):
        n = A.shape[0]

        I = identity_matrix(n)

        B = identity_matrix(n)

        coeffs = [Fraction(1)]

        for k in range(1, n + 1):

            AB = A @ B

            c = -trace(AB) / k

            coeffs.append(c)

            B = AB + c * I

        return coeffs
        

    def get_eigenvalues(A):
        coeffs = characteristic_polynomial(A)

        constant = coeffs[-1]

        roots = []

        for d in divisors(int(constant)):

            if polynomial_value(coeffs, Fraction(d)) == 0:
                roots.append(Fraction(d))

        if polynomial_value(coeffs, Fraction(0)) == 0:
            roots.append(Fraction(0))

        