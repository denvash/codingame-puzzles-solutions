// https://www.codingame.com/training/easy/blowing-fuse

const r = () =>
  readline()
    .split(' ')
    .map(Number)

const [, , capacity] = r()
const consumptions = r()
const sequence = r()

let maxPower = 0
let power = 0

sequence.forEach(device => {
  const id = device - 1
  power += consumptions[id]
  consumptions[id] = -consumptions[id]
  if (power > maxPower) maxPower = power
})

const isBlown = maxPower > capacity
console.log(`Fuse was${isBlown ? '' : ' not'} blown.`)
if (!isBlown) console.log(`Maximal consumed current was ${maxPower} A.`)
