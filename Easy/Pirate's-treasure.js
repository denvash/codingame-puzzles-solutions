// https://www.codingame.com/ide/puzzle/pirates-treasure

const [, H] = [+readline(), +readline()];
const map = [...new Array(H)]
    .map(() => readline().split(' ').map(Number));

const getSquare = (i, j) => map[i] === undefined ? undefined : map[i][j];

mainLoop:
for (let i = 0; i < map.length; i++) {
    for (let j = 0; j < map[i].length; j++) {
        if (map[i][j] !== 0) continue;
        const obstacles = [
            getSquare(i-1, j-1), getSquare(i-1, j), getSquare(i-1, j+1),
            getSquare(i, j-1), getSquare(i, j+1), getSquare(i+1, j-1),
            getSquare(i+1, j), getSquare(i+1, j+1),
        ];
        if (obstacles.includes(0) ||
            obstacles.filter((x) => isNaN(x)).length > 5) continue;
        console.log(j, i);
        break mainLoop;
    }
}
