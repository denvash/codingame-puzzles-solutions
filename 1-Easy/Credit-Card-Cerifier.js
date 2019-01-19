// https://www.codingame.com/training/easy/credit-card-verifier-luhns-algorithm

const cards = [...Array(+readline())].map(() =>
  readline()
  .split(' ')
  .join('')
  .split('')
  .map(Number)
)

cards.forEach(card => {
  const result = card.reduce((sum, curr, i) => {
    if (i % 2 === 0) {
      curr *= 2
      if (curr >= 10) curr -= 9
    }
    return sum + curr
  }, 0)
  console.log(result % 10 === 0 ? 'YES' : 'NO')
})