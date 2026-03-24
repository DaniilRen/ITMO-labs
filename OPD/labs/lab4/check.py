from functools import lru_cache

@lru_cache
def F(x):
    if x in range(-2726, 0):
        return -2726
    return 5*x + 77

def R(x, y, z):
    return F(y) - F(x-1) + F(z) - 1

print(R(-6568, -6569, -6568))
print(R(-2725, 6538, -561))