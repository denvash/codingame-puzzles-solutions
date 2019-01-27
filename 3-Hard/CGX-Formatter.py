import sys
import math

# https://www.codingame.com/training/hard/cgx-formatter

def check_next_line(c, last_char):
    global temp_string
    global tabs
    if last_char == '=':
        print(ord(c), file=sys.stderr)
    if last_char == ')' and c != ';' and c != ')' and c != '(':
        temp_string += '\n' + (tabs * 4) * " "
    elif last_char == '(' and c != ")":
        temp_string += '\n' + (tabs * 4) * " "
    elif last_char == ';':
        temp_string += '\n' + (tabs * 4) * " "
    elif last_char == '=' and c == '(':
        temp_string += '\n' + (tabs * 4) * " "


cgxlines = []
n = int(input())
for i in range(n):
    cgxline = input()
    cgxlines.append(cgxline)

tabs = 0
temp_string = ''
string_started = False
last_char = ''
for line in cgxlines:
    for c in line:
        if c == '\'' and not (string_started):
            check_next_line(c, last_char)
            string_started = True
            temp_string += c
        elif string_started and c == '\'':
            string_started = False
            temp_string += c
        elif string_started:
            temp_string += c
        elif (c.isspace()):
            continue
        else:
            check_next_line(c, last_char)
            if c == '(':
                temp_string += "("
                tabs += 1
            elif c == ')':
                tabs -= 1
                temp_string += '\n' + (tabs * 4) * " " + ")"
            else:
                temp_string += c
        last_char = c
print(temp_string)