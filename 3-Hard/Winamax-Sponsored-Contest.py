# https://www.codingame.com/training/hard/winamax-sponsored-contest
# https://github.com/Mormehtar/codingame/blob/ac097ba1212dffb6d4aa89926105722c51a79b75/Winamax/first.py

import sys
import math
import copy

HOLE = "H"
HAZARD = "X"
EMPTY = "."

UP = "^"
DOWN = "v"
LEFT = "<"
RIGHT = ">"

DIRECTIONS_LIST = [UP, DOWN, LEFT, RIGHT]

OPPOSITES = {
    UP: DOWN,
    DOWN: UP,
    LEFT: RIGHT,
    RIGHT: LEFT
}

DIRECTIONS = {
    UP: [0, -1],
    DOWN: [0, 1],
    LEFT: [-1, 0],
    RIGHT: [1, 0]
}


class Map:
    def __init__(self, field_data, size):
        self.field = field_data
        self.size = size
        self.balls = []
        self.holes = []
        self._clean()

    def clone(self):
        return copy.deepcopy(self)

    def __str__(self):
        def simplify(char):
            if char in DIRECTIONS_LIST:
                return char
            return EMPTY

        output = list(map(list, zip(*self.field)))
        return '\n'.join([''.join(map(simplify, output[i])) for i in range(len(output))])

    def _clean(self):
        for i in range(self.size[0]):
            for j in range(self.size[1]):
                if self.field[i][j] == HOLE:
                    self.field[i][j] = Hole(i, j)
                    self.holes.append(self.field[i][j])
                elif self.field[i][j].isdigit():
                    self.field[i][j] = Ball(int(self.field[i][j]), i, j)
                    self.balls.append(self.field[i][j])

    def isvalid(self, point):
        return 0 <= point[0] < self.size[0] and 0 <= point[1] < self.size[1]

    def _build_possible_paths(self, power, path, last_direction=None):
        directions = [direction for direction in DIRECTIONS.keys() if last_direction is None or direction != OPPOSITES[last_direction]]
        output = []
        for direction in directions:
            new_path = self.check_move(path.clone(), direction, power)
            if not new_path.valid:
                continue
            if new_path.is_finished():
                output.append(new_path)
                continue
            if power == 1:
                continue
            output += self._build_possible_paths(power - 1, new_path, direction)
        return output

    def build_paths(self):
        for ball in self.balls:
            paths = self._build_possible_paths(ball.power, Path([ball.x, ball.y]))
            ball.load_paths(paths)
        clean_balls = []
        clean_holes = []
        other = []
        for ball in self.balls:
            ball.paths.sort(key=lambda a: a.hole.weight)
            if len(ball.paths) == 1:
                clean_balls.append(ball)
                continue
            if ball.paths[0].hole.weight == 1:
                clean_holes.append(ball)
                continue
            other.append(ball)
        other.sort(key=lambda a: len(a.paths))
        self.balls = clean_balls + clean_holes + other

    def check_move(self, path, direction, power):
        modifier = DIRECTIONS[direction]
        last_point = path.get_end()
        new_point = [last_point[i] + power * modifier[i] for i in range(2)]
        if not self.isvalid(new_point):
            path.invalidate()
        else:
            target = self.field[new_point[0]][new_point[1]]
            if isinstance(target, Hole):
                path.reach(new_point, direction, target)
                target.increase_weight()
            elif target == HAZARD or isinstance(target, Ball) or target.isdigit():
                path.invalidate()
            else:
                path.add_point(new_point, direction)
        return path

    def make_line(self, path, target):
        direction = path.directions[target]
        vector = DIRECTIONS[direction]
        begin = path.path[target]
        end = path.path[target + 1]
        now = begin[:]
        while now[0] != end[0] or now[1] != end[1]:
            if isinstance(self.field[now[0]][now[1]], Hole):
                return False
            path.backup(now[0], now[1], self.field[now[0]][now[1]])
            self.field[now[0]][now[1]] = direction

            now[0] += vector[0]
            now[1] += vector[1]
            if self.field[now[0]][now[1]] in DIRECTIONS_LIST or isinstance(self.field[now[0]][now[1]], Ball):
                return False
        return True

    def implement_path(self, path):
        for i in range(0, len(path.path) - 1):
            success = self.make_line(path, i)
            if not success:
                path.restore(self)
                return False
        return True

    def search_valid_path(self):
        ball_index = 0
        path_index = 0
        revert_data = []
        while 0 <= ball_index < len(self.balls):
            if path_index >= len(self.balls[ball_index].paths):
                if len(revert_data) == 0:
                    # print('Path not found!')
                    raise Exception('Path not found!')
                    break
                revert_element = revert_data.pop()
                revert_element[1].restore(self)
                revert_element[1].hole.filled = False

                ball_index -= 1
                path_index = revert_element[0] + 1
                continue
            # self.balls[ball_index].test_print()
            # self.balls[ball_index].paths[path_index].test_print()
            if self.balls[ball_index].paths[path_index].hole.filled:
                # self.balls[ball_index].paths[path_index].hole.test_print()
                step_valid = False
            else:
                step_valid = self.implement_path(self.balls[ball_index].paths[path_index])
            if step_valid:
                # self.balls[ball_index].paths[path_index].hole.test_print()
                self.balls[ball_index].paths[path_index].hole.filled = True
                revert_data.append([path_index, self.balls[ball_index].paths[path_index]])
                ball_index += 1
                path_index = 0
                continue
            else:
                path_index += 1


class Path:
    def __init__(self, start):
        self.valid = True
        self.hole = None
        self.path = [start]
        self.directions = []
        self.backups = []

    def __str__(self):
        return '{}, {}, {}'.format(self.path, self.directions, self.valid)

    def test_print(self):
        print('Path starts at: {}, {}, length: {}, it is valid {} it is finished {}'.format(self.path[0][0], self.path[0][1], len(self.path), self.valid, self.is_finished()))

    def backup(self, x, y, target):
        self.backups.append([x, y, target])

    def restore(self, local_field):
        for element in self.backups:
            local_field.field[element[0]][element[1]] = element[2]
        self.backups = []

    def clone(self):
        return copy.deepcopy(self)

    def get_end(self):
        return self.path[-1]

    def add_point(self, point, direction):
        self.path.append(point)
        self.directions.append(direction)

    def invalidate(self):
        self.valid = False

    def reach(self, point, direction, hole):
        self.add_point(point, direction)
        self.hole = hole

    def is_finished(self):
        return self.hole is not None


class Ball:
    def __init__(self, power, x, y):
        self.power = power
        self.x = x
        self.y = y
        self.paths = []

    def load_paths(self, paths):
        self.paths = paths

    def __str__(self):
        return str(self.power)

    def test_print(self):
        print('Power: {}, Coordinates: {}, {} Paths: {}'.format(self.power, self.x, self.y, len(self.paths)))


class Hole:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.weight = 0
        self.filled = False

    def increase_weight(self):
        self.weight += 1

    def test_print(self):
        print('Hole', self.x, self.y, self.weight, self.filled)

    def __str__(self):
        return HOLE


def get_input():
    width, height = [int(i) for i in input().split()]
    return [list(input()) for i in range(height)], width, height


def simulate_data(str_map):
    resulting_field = [list(row) for row in str_map.split('\n')]
    calculated_height = len(resulting_field)
    calculated_width = len(resulting_field[0])
    return resulting_field, calculated_width, calculated_height


def test_print(input):
    print('*' * 40)
    print('\n'.join([''.join(i) for i in input]))
    print('*' * 40)


def solve(input_map, width, height):
    field = Map(list(map(list, zip(*input_map))), [width, height])
    field.build_paths()
    field.search_valid_path()
    print(field)

if __name__ == '__main__':
    transposed_field, width, height = get_input()
    # test_print(transposed_field)
    solve(transposed_field, width, height)