// https://www.codingame.com/ide/puzzle/counting-squares-on-pegs

const pegs = [...new Array(+readline())]
    .map((_) => {
        const [x, y] = readline().split(' ').map(Number);
        return {x: x, y: y};
    });

let squares = 0;

// for loop faster then foreach.
for (let i = 0; i < pegs.length; i++) {
    for (let j = 0; j < pegs.length; j++) {
        const p1 = pegs[i];
        const p2 = pegs[j];
        const dx = p2.x - p1.x;
        const dy = p2.y - p1.y;
        if (
            (JSON.stringify(p1) === JSON.stringify(p2)) ||
            (dx < 0 || dy < 0) ||
            (!pegs.some((e) => e.x === p2.x - dy && e.y === p2.y + dx)) ||
            (
                !pegs.some((e) => e.x === p1.x - dy && e.y === p1.y + dx) ||
                dx === 0 && p1.x - dy < p2.x
            )
        ) continue;
        squares++;
    }
}

console.log(squares);
