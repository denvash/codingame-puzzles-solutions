# adopted from https://github.com/JohnPrime/CodinGame/blob/master/Surface/Surface.py
import sys
import math

l = int(input())
h = int(input())


def search(map, x, y):
    visit_map = [[False] * l for i in range(h)]

    list = [[x, y]]

    surface = 0

    while len(list) != 0:
        cell = list[len(list) - 1]
        list.pop()

        if visit_map[cell[1]][cell[0]] == False:
            visit_map[cell[1]][cell[0]] = True

            if map[cell[1]][cell[0]] == 'O':
                surface += 1

                if cell[0] > 0:
                    if visit_map[cell[1]][cell[0] - 1] == False and map[cell[1]][cell[0] - 1] == 'O':
                        list.append([cell[0] - 1, cell[1]])
                if cell[0] < l - 1:
                    if visit_map[cell[1]][cell[0] + 1] == False and map[cell[1]][cell[0] + 1] == 'O':
                        list.append([cell[0] + 1, cell[1]])
                if cell[1] > 0:
                    if visit_map[cell[1] - 1][cell[0]] == False and map[cell[1] - 1][cell[0]] == 'O':
                        list.append([cell[0], cell[1] - 1])
                if cell[1] < h - 1:
                    if visit_map[cell[1] + 1][cell[0]] == False and map[cell[1] + 1][cell[0]] == 'O':
                        list.append([cell[0], cell[1] + 1])
    return surface

map = []
for i in range(h):
    row = input()
    map.append(row)

n = int(input())
for i in range(n):
    x, y = [int(j) for j in input().split()]
    print(search(map, x, y))