// https://www.codingame.com/ide/puzzle/organic-compounds

let c = [...new Array(+readline())].map(readline);
let answer = 'VALID';

loop1:
    for (let i = 0; i < c.length; i++) {
        const n = c[i].length;
        for (let k = 0; k < n; k++) {
            const N = +c[i][k];
            const UP = i - 1 > 0 && k - 1 > 0 && k - 1 < c[i - 1].length ?
                +c[i - 1][k - 1] : 0;
            const DOWN = i + 1 < c.length &&
                k - 1 > 0 && k - 1 < c[i + 1].length ? +c[i + 1][k - 1] : 0;
            const RIGHT = k + 2 < n ? +c[i][k + 2] : 0;
            const LEFT = k - 4 > 0 ? +c[i][k - 4] : 0;
            if (c[i].slice(k - 2, k) === 'CH') {
                if (N + UP + RIGHT + LEFT + DOWN !== 4) {
                    answer = 'INVALID';
                    break loop1;
                }
            }
        }
    }

print(answer);
