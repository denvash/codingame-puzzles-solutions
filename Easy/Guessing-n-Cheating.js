// https://www.codingame.com/ide/puzzle/guessing-n-cheating

const N = +readline();
let l = 0;
let r = 100;
const findEvidence = (round) => {
    if (round > N) return false;
    const [n, , action] = readline().split(' ')
        .map((c) => !isNaN(c) ? Number(c) : c);
    if (action === 'low') {
        if (n >= r) return round; if (n > l) l = n;
    } else if (action === 'high') {
        if (n <= l) return round; if (n < r) r = n;
    } else if (action === 'on') if (!(l < n && n < r)) return round;
    if (r - l <= 1) return round;
    return findEvidence(round + 1);
};

const i = findEvidence(1);
console.log(i ? 'Alice cheated in round ' + i : 'No evidence of cheating');
