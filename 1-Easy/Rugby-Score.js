// https://www.codingame.com/training/easy/rugby-score

const N = parseInt(readline())

for (let i = 0; i <= N / 5; ++i) {
  for (let j = 0; i >= j; ++j) {
    for (let k = 0; k <= N / 3; ++k) {
      if (i * 5 + j * 2 + k * 3 == N) print([i, j, k].join(' '))
    }
  }
}