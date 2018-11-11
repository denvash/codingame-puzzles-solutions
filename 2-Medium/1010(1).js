// https://www.codingame.com/ide/puzzle/10101

const [W, H] = [+readline(), +readline()];
const a = [...Array(H)].map(() => readline().split(''));

let total = 0;
for (let i = 1; i < H; i++) {
    const grid = JSON.parse(JSON.stringify(a));
    let sum = 0;
    for (let j = 0; j < W; j++) {
        if (
            grid[i][j] === '.' &&
            grid[i][j+1] === '.' &&
            grid[i-1][j] === '.' &&
            grid[i-1][j+1] === '.'
        ) {
            grid[i][j] = '#';
            grid[i][j+1] = '#';
            grid[i-1][j] = '#';
            grid[i-1][j+1] = '#';
            sum+=grid.reduce((total, line) => {
                if (line.every((square) => square === '#')) total++;
                return total;
            }, 0);
            for (let i = 0; i < W; i++) {
                let sameCol = true;
                for (let j = 0; j < H; j++) {
                    if (grid[j][i] !== '#') {
                        sameCol = false;
                        break;
                    }
                }
                if (sameCol) sum++;
            }
            total = Math.max(total, sum);
        }
    }
}

console.log(total);
