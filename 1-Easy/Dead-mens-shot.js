// https://www.codingame.com/training/easy/dead-mens-shot

const isPointInsidePolygon = ([x, y], polyArray) => {
    let inside = false;
    for (let i = 0, j = polyArray.length - 1; i < polyArray.length; j = i++) {
        const xi = polyArray[i][0]; const yi = polyArray[i][1];
        const xj = polyArray[j][0]; const yj = polyArray[j][1];

        const intersect = ((yi > y) != (yj > y))
            && (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
        if (intersect) inside = !inside;
    }

    return inside;
};

const readlineToPoint = () => readline().split(' ').map(Number);
const polygon = [...Array(+readline())].map(readlineToPoint);
const shots = [...Array(+readline())].map(readlineToPoint);

shots.map((p) => isPointInsidePolygon(p, polygon) === true? 'hit' : 'miss')
    .forEach((res) => console.log(res));

