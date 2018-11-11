// https://www.codingame.com/ide/puzzle/morellets-random-lines

Array.prototype.unique = () => Array.from(new Set(this));
const mapToNumbers = _ => readline().split(' ').map(Number);
const [xA, yA, xB, yB] = mapToNumbers();
const getY = (x, [a, b, c]) => -((a * x + c) / b)
const getYs = (a) => [getY(xA, a), getY(xB, a)];
const reductionByMin = (a) => a.every(n => n % Math.min(...a) === 0) ? a.map(e => e / Math.min(...a)) : a;
const sameSign = (a) => a[0] < 0 ? a.map(n => -1 * n) : a;

const eqs = [...new Array(+readline())]
    .map(mapToNumbers)
    .map(reductionByMin)
    .map(sameSign)
    .map(e => e.toString()) // Can use set only on strings.
    .unique()
    .map(e => e.split(',').map(Number));

let cntA = 0,
    cntB = 0;

// Count how many lines the line (0,yA),(0,yB) crosses,
// if both counters are even/odd, the points in same color.
eqs.map(getYs).forEach(([_yA, _yB]) => {
    if (yA === _yA || yB === _yB) {
        print('ON A LINE');
        quit();
    }
    if (yA > _yA) cntA++;
    if (yB > _yB) cntB++;
})

print(cntA % 2 === cntB % 2 ? 'YES' : 'NO');