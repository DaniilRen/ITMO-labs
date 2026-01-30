from re import *
p = r'\[.*\]'
print(findall(p, 'int[][] arr = new int[10][10]'))