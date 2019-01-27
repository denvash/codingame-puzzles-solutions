# https://www.codingame.com/training/hard/bender-episode-2

def get(number):
    global room_best_cost
    global rooms
    room_best = room_best_cost.get(number, None)
    if room_best is not None:
        return room_best
    else:
        room = rooms[number]
        rid_s_1 = rid_s_2 = 0
        if room['rid1']=='E':
            rid_s_1 = int(room['money'])
        else:
            rid_s_1 = int(room['money']) + get(room['rid1'])
        if room['rid2']=='E':
            rid_s_2 = int(room['money'])
        else:
            rid_s_2 = int(room['money']) + get(room['rid2'])
        room_best_cost_number = (rid_s_1, rid_s_2) [rid_s_1<rid_s_2]
        room_best_cost[number] = room_best_cost_number
    return room_best_cost_number

rooms = {}
room_best_cost = {}
n = int(input())
for i in range(n):
    rid, money, rid1, rid2 = input().split()
    rooms[rid] = {'money':money, 'rid1':rid1, 'rid2':rid2}
print(get('0'))