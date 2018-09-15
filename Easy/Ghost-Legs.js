// https://www.codingame.com/ide/puzzle/ghost-legs

const charSplit = (c) => readline().split(c);
const [, H] = charSplit(' ');
const T = charSplit('  ');

// make a readable table
const getLine = () => readline()
    .replace(/ {2}/g, '0')
    .replace(/-{2}/g, '1')
    .split('')
    .map((e) => e === '0' ? false : e === '1' ? true : '|');

const table = [...new Array(H - 2)].map(getLine);
const B = charSplit('  ');

T.forEach((t, k) => {
    k*=2;
    table.forEach((line) => {
        const moveRight = line[k + 1];
        const moveLeft = line[k - 1];
        k = moveRight ? k + 2 : moveLeft ? k - 2 : k;
    });
    console.log(`${t}${B[k/2]}`);
});
