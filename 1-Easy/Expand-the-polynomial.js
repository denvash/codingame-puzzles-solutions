// https://www.codingame.com/ide/puzzle/expand-the-polynomial

let brackets =
    readline()
    .match(/[^\(\)*]+/g)
    .map((v) => v.match(/(\+|\-)?[a-z0-9.^]+/gi));

brackets.forEach((e, i) => {
    if (/^\^\d/gm.test(e)) {
        const n = +e.toString().substring(1);
        brackets.splice(i, 1);
        const multipliedArr = Array(n - 1).fill(brackets[i - 1]);
        brackets = brackets.concat(multipliedArr);
    }
});

const compareDegree = (a, b) => a.deg < b.deg ? -1 : a.deg > b.deg ? 1 : 0;

const toEofsArray = (poly) => {
    const arr = [];
    poly.forEach((p) => {
        const [e, index] = p.split(/[x?\^?]+/g);
        arr.push({
            deg: +index,
            eof: (e === 'x' || e==='+' || e === '') ? 1 :
                (e === '-x' || e === '-' ? -1 : +e),
        });
    });
    arr.sort(compareDegree);
    return arr;
};

const eofsObjects =
    brackets
    .map((arg) => arg.map(
        (e) => e.includes('x') ? (e.includes('^') ? e : e + '^1') : e + '^0')
        )
    .map(toEofsArray);

const multiply = (a, b) => {
    const result = [];
    a.forEach((x) => {
        b.forEach((y) => {
            result.push({deg: x.deg + y.deg, eof: x.eof * y.eof});
        });
    });
    return result;
};

const multiplyBrackets = eofsObjects.reduce(multiply)
    .sort(compareDegree)
    .reverse();

const collectDeg = (arr, x, i) => {
    if (i === 0) return arr;
    let last = arr[arr.length - 1];
    if (last.deg === x.deg) last.eof += x.eof;
    else arr.push(x);
    return arr;
};
const collect = multiplyBrackets.reduce(collectDeg, [multiplyBrackets[0]]);

collect.forEach((e) => e.eof = `${e.eof > 0 ? '+' : ''}${e.eof}`);
const answer = collect.reduce((a, b, i) => {
    if (b.eof === '0') return a;
    let eof = b.eof === '+1' ? '+' : b.eof === '-1' && b.deg > 0 ? '-' : b.eof;
    const deg = b.deg === 0 ? '' : b.deg === 1 ? 'x' : `x^${b.deg}`;
    if (i === 0 && eof[0] === '+') eof = eof.slice(1);
    a += `${eof}${deg}`;
    return a;
}, '');

console.log(answer);
