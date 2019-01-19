// https://www.codingame.com/training/easy/carmichael-numbers

const N = +readline();

let isPrime = true;
let isCarmichael = true;

const computePow = (n, e, m) => {
  if (e == 0) return 1;
  if (e == 1) return n % m;
  const sq = (n * n) % m;
  if (e & 1) return (n * computePow(sq, e >> 1, m)) % m;
  return computePow(sq, e >> 1, m);
};

for (let i = 2; isCarmichael && i < N; i++) {
  isPrime &= N % i != 0;
  isCarmichael &= computePow(i, N, N) == i;
}

console.log(!isPrime && isCarmichael ? 'YES' : 'NO');
