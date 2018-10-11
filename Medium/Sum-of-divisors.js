// https://www.codingame.com/ide/puzzle/sum-of-divisors

const N = +readline();
console.log([...Array(N+1).keys()].reduce((s, e) => s+Math.floor(N/e)*e));
