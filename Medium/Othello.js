// https://www.codingame.com/ide/puzzle/othello

let board = [...new Array(8)].map(() => readline().split(''));
const [color, move] = readline().split(' ');

const toBoardIndexes = (char) => [char[1] - 1, char.charCodeAt(0) - 97];
const [x, y] = toBoardIndexes(move);

const mirror = (a) => {
    let m = a.join('').match(/BW+(?=b)|bW+(?=B)|WB+(?=w)|wB+(?=W)/g);
    return m ? m.reduce((s, a) => s + a.length - 1, 0) : 0;
};

const checkAllDirections = (x, y) => mirror(board[y]) +
    mirror(board.map((a) => a[x])) +
    mirror(board.map((a, i) => a[x - y + i])) +
    mirror(board.map((a, i) => a[x + y - i]));

const squareBeforeChange = board[x][y];
board[x][y] = color.toLowerCase();
const c = checkAllDirections(y, x);
board = board.join();
const tokens = {W: board.match(/W/gi).length, B: board.match(/B/gi).length};
tokens[color] += c;
tokens[color === 'W' ? 'B' : 'W'] -= c;

console.log(
    squareBeforeChange !== '-' ? 'NOPE' :
    c !== 0 ? `${tokens.W} ${tokens.B}` :
    'NULL'
);
