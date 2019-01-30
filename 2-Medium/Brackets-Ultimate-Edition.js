// https://www.codingame.com/ide/puzzle/brackets-ultimate-edition

// node.js environment
// const inputFile = 'lib/1.in';
// const readFileSync = require('fs').readFileSync;
// const inArray = readFileSync(inputFile, 'utf8').split('\n');
// const readline = () => inArray.shift();

const exprs = [...Array(+readline())].map(() =>
  readline()
    .match(/[\(\)\[\]\{\}\<\>]/g) // Focus on brackets.
    .join('')
);

// Match full pair like '()', worth 0 points
// Match long pair like '()' in '({)', worth 1 point
// Match mirror pair like '}{', worth 2 points
const regex = [/\(\)|\{\}|\[\]|<>/g, /([(){}[\]<>])\1/, /\)\(|\}\{|\]\[|></];

exprs.forEach(expr => {
  let sum = 0;

  // Reduce as much as possible.
  for (let i = 0; i < regex.length; ++i) {
    const reducedExpr = expr.replace(regex[i], '');
    const isReduced = reducedExpr !== expr;
    if (isReduced) {
      expr = reducedExpr; // Update
      sum += i; // Add points
      i = -1; // Reset
    }
  }
  console.log(expr.length ? -1 : sum);
});
