from functools import lru_cache

@lru_cache
def F(x):
    if -2726 <= x < 0: return -2726
    return 5*x + 77

def R(y, x, z):
    return F(y) - F(x-1) + F(z) - 1

print(R(-2726, 5448, -2726))
print(R(2988, -2725, 2989))