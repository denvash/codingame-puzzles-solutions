# https://www.codingame.com/training/hard/the-labyrinth

"""
    This code uses BFS algorithm to find shortest path between Kirks position
    and our target. At first our targets are all not-scanned cells (and we
    treat Console room as wall). Then, if we cannot go to more not-scanned cells
    we find shortest path to console room, and later to our starting position.
    That way (discovering whole map before going to C) we are 100 percent sure
    that we won't trigger alarm.
    Only concern may be fuel, but it doesn't seem problematic in given test cases.
    By Hubert Jarosz
"""

INFINITY = float('inf')

# checks if given coordinate is on map
def is_on_map(v):
    if v[0] < 0 or v[0] >= r or v[1] < 0 or v[1] >= c:
        return False
    return True

# gives map field neighbours that are on map
def get_neighbours(vertex):
    vr, vc = vertex
    first_set = {(vr-1, vc), (vr+1, vc), (vr, vc-1), (vr, vc+1)}

    return {v for v in first_set if is_on_map(v)}

# traces our first step from BFS path
def first_step(parent, start, n):
    v = n
    while parent[v[0]][v[1]] != start:
        v = parent[v[0]][v[1]]
    return v

# BFS algorithm for The Labyrinth game
def BFS(game_map, start, goal):

    # preparation
    queue = []
    colour = []
    distance = []
    parents = []
    for row in range(len(game_map)):
        colour.append([])
        distance.append([])
        parents.append([])
        for collumn in range(len(game_map[row])):
            colour[row].append(0)
            distance[row].append(INFINITY)
            parents[row].append(None)

    colour[start[0]][start[1]] = 1
    distance[start[0]][start[1]] = 0

    queue.append(start)

    # algorithm loop
    while queue != []:
        u = queue.pop(0)
        forbidden_symbols = ['#']
        if goal == '?':
            forbidden_symbols.append('C')
        neighbours = {x for x in get_neighbours(u) if game_map[x[0]][x[1]] not in forbidden_symbols}
        for n in neighbours:
            if colour[n[0]][n[1]] == 0:
                colour[n[0]][n[1]] = 1
                distance[n[0]][n[1]] = distance[u[0]][u[1]] + 1
                parents[n[0]][n[1]] = u
                queue.append(n)
                if game_map[n[0]][n[1]] == goal:
                    return first_step(parents, start, n)
        colour[u[0]][u[1]] = 2

    return None


def where_to_go(game_map, start, back):
    if not back:
        go_to = BFS(game_map, start, '?')
        if go_to == None:
            return BFS(game_map, start, 'C')
        return go_to
    else:
        return BFS(game_map, start, 'T')

r, c, a = [int(i) for i in input().split()]

# game loop
back = False
while True:
    # kr: row where Kirk is located.
    # kc: column where Kirk is located.
    kr, kc = [int(i) for i in input().split()]
    rows = []
    for i in range(r):
        rows.append(input())

    if rows[kr][kc] == 'C':
        back = True
        alarm = a

    go_to = where_to_go(rows, (kr, kc), back)

    if go_to[0] > kr:
        print("DOWN")
    elif go_to[0] < kr:
        print("UP")
    elif go_to[1] > kc:
        print("RIGHT")
    else:
        print("LEFT")