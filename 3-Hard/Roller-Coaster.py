# https://www.codingame.com/training/hard/roller-coaster

L, C, N = [int(i) for i in input().split()]
text = []
idx = 0
memory = {}
for i in range(N):
    Pi = int(input())
    text.append(Pi)
num = 0
while C > 0:
    origIdx = idx
    ride = 0
    try:
        num+=memory[idx][0]
        idx = memory[idx][1]
    except KeyError:
        Amount = 0
        while ride+text[idx]<=L:
            ride+=text[idx]
            num+=text[idx]
            Amount+=text[idx]
            idx = int((idx+1)%len(text))
            if idx == origIdx:
                break
        memory[origIdx] = [Amount, idx]
    C-=1
print(num)