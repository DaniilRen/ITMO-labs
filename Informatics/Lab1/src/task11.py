
def convert(n: int) -> str:
    digits = []
    default = n
    while n != 0:
        n, remainder = divmod(n, -10)
        if remainder < 0:
            n += 1
            remainder += 10
        digits.append(str(remainder))
    return ''.join(digits[::-1])


if __name__ == "__main__":
    print(convert(703))



