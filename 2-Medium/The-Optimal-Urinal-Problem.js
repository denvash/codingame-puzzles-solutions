// https://www.codingame.com/ide/puzzle/the-optimal-urinal-problem

const n = +readline();

// normal recursion doesn't work because of overflow, so will use memoization.
const memo = [];

const findTotal = (first, last) => {
    const dist = last-first;
    if (dist <= 3 ) return 0;
    const mid = Math.floor((first+last)/2);
    memo[dist] =
        memo[dist] ?
            memo[dist] : findTotal(first, mid) + findTotal(mid, last) + 1;
    return memo[dist];
};

const answer = n === 4 || n === 3 ? {max: 2, index: 1}: {max: 1, index: 1};
let i = 0;

// duplicate answers after n/2.
while (n > 4 && i <= Math.floor(n/2)) {
    const total =
        (memo[i] ? memo[i] : findTotal(0, i)) +
            (memo[n-1-i] ? memo[n-1-i] : findTotal(i, n-1)) +
                (i > 1 ? 3 : 2);
    if (answer.max < total) {
        answer.max = total;
        answer.index = i+1;
    }
    i++;
}

console.log(answer.max, answer.index);
