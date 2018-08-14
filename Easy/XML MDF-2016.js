/*
CodInGame Puzzle Solution
XML MDF-2016
Difficulty: Easy
https://www.codingame.com/ide/puzzle/xml-mdf-2016
*/

const SEPERATOR = '-';
const weights = new Map();
let depth = 0;
readline().replace(/-./g, SEPERATOR).split('').forEach(c => {
    if (!weights.has(c) && c !== SEPERATOR) weights.set(c,0);
    if (c === SEPERATOR) depth--;
    else weights.set(c, weights.get(c) + 1 / (++depth));
});
print([...weights.keys()].reduce((a, b) => weights.get(a) > weights.get(b) ? a : b));
