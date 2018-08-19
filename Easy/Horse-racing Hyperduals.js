// https://www.codingame.com/ide/puzzle/horse-racing-hyperduals

const VECTORS = [...Array(+readline())].map(_ =>
  readline()
  .split(' ')
  .map(Number)
);

const distance = ([v1, e1], [v2, e2]) => Math.abs(v2 - v1) + Math.abs(e2 - e1);

const curriedReduce = curr1 => (acc2, curr2) =>
  Math.min(curr1 === curr2 ? Infinity : distance(curr1, curr2), acc2);

const minReduce = (acc1, curr1) =>
  Math.min(acc1, VECTORS.reduce(curriedReduce(curr1), Infinity));

print(VECTORS.reduce(minReduce, Infinity));