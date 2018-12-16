// https://www.codingame.com/training/easy/rectangular-block-spinner

const size = +readline();
const angle = (+readline() % 360 + 360) % 360;

const block = [...Array(size)].map(readline);
const outputSize = 2 * size - 1;
const output = [...Array(outputSize)].map(() => Array(outputSize).fill(' '));

let startX; let startY; let dx; let dy; let rx; let ry;
switch (angle) {
    case 45: [startX, startY, dx, dy, rx, ry] = [0, size-1, 1, -1, 1, 1]; break;
    case 135: [startX, startY, dx, dy, rx, ry] = [size-1, 2*size-2, -1, -1, 1, -1]; break;
    case 225: [startX, startY, dx, dy, rx, ry] = [2*size-2, size-1, -1, 1, -1, -1]; break;
    case 315: [startX, startY, dx, dy, rx, ry] = [size-1, 0, 1, 1, -1, 1]; break;
}

let cx = startX; let cy = startY;
for (let i = 0; i < size; i++) {
    for (let j = 0; j < size; j++) {
        output[cy][cx] = block[i][j*2];
        cx += dx;
        cy += dy;
    }
    cx += - size * dx + rx;
    cy += - size * dy + ry;
}
console.log(output.map((row) => row.join('')).join('\n'));
