// https://www.codingame.com/training/medium/digit-sum-successor

const N = +readline();

const digitsArrayOf = number => Array.from(number.toString(), Number);

const sumOfDigits = number =>
  digitsArrayOf(number).reduce((sum, curr) => (sum += curr));

const lastSignificantDigitIndex = number =>
  digitsArrayOf(number).reduce(
    ({ sum, lastIndex }, curr, i) => ({
      sum: sum === 0 ? sum : sum - curr,
      lastIndex: sum === 0 ? lastIndex : i
    }),
    {
      sum: sumOfDigits(N),
      lastIndex: -1
    }
  ).lastIndex;

const nextRounded = number => {
  const i = lastSignificantDigitIndex(number);
  const digitsArray = digitsArrayOf(number);
  const factor = digitsArray.length - 1 - i;
  const toAdd = (10 - digitsArray[i]) * 10 ** factor;
  return number + toAdd;
};

const nextTarget = number => {
  const numArray = digitsArrayOf(number).reverse();
  let currentSum = sumOfDigits(number);
  const targetSum = sumOfDigits(N);
  let i = 0;

  // Increase number in O(n) complexity
  while (currentSum !== targetSum) {
    const nextToAdd = targetSum - currentSum;
    numArray[i++] = nextToAdd >= 10 ? 9 : nextToAdd;
    number = Number([...numArray].reverse().join(''));
    currentSum = sumOfDigits(number);
  }
  return number;
};

console.log(nextTarget(nextRounded(N)));
