// https://www.codingame.com/ide/puzzle/xml-mdf-2016

const SEPARATOR = '-';
const weights = new Map();
let depth = 0;
readline().replace(/-./g, SEPARATOR).split('').forEach(c => {
    if (!weights.has(c) && c !== SEPARATOR) weights.set(c,0);
    if (c === SEPARATOR) depth--;
    else weights.set(c, weights.get(c) + 1 / (++depth));
});
print([...weights.keys()].reduce((a, b) => weights.get(a) > weights.get(b) ? a : b));
