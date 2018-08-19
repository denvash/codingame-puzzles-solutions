// https://www.codingame.com/ide/puzzle/the-travelling-salesman-problem

const dist = (p, q) => Math.sqrt(((p.x - q.x) ** 2) + ((p.y - q.y) ** 2));
const point = ([s1, s2]) => ({ x: +s1,y: +s2 });

let cities = [...new Array(+readline())].map(_ => point(readline().split(' ').map(Number)));

const first = cities.shift();
let total = 0,
    next = first;

while (cities.length > 1) {
    const distances = cities.map(p => dist(next, p));
    const i = distances.indexOf(Math.min(...distances));
    total += dist(next, cities[i]);
    next = cities.splice(i, 1)[0];
}

print(Math.round(total + dist(next, cities[0]) + dist(first, cities[0])));