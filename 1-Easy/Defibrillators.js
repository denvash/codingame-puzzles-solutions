// https://www.codingame.com/ide/puzzle/defibrillators

String.prototype.toPoint = function () { return this.replace(/,/g, '.'); }

const xA = +(readline().toPoint());
const yA = +(readline().toPoint());

const defibs = [...new Array(+readline())].map(() => readline().split(';'));
defibs.forEach((def) => {
    def[4] = def[4].toPoint();
    def[5] = def[5].toPoint();
});

const toRadians = (angle) => angle * (Math.PI / 180);

const getDistance = (xA, yA, xB, yB) => {
    const x = (xB-xA) * Math.cos((toRadians(yA)+toRadians(yB)) / 2);
    const y = yB-yA;
    return Math.sqrt(x**2+y**2) * 6371;
};

console.log(defibs.reduce((acc, def) => {
    const dist = getDistance(xA, yA, def[4], def[5]);
    if (acc.dist === -1 || acc.dist > dist) {
        acc.name = def[1];
        acc.dist = dist;
    }
    return acc;
}, {name: '', dist: -1}).name);
