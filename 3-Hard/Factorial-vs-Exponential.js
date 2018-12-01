// https://www.codingame.com/training/hard/factorial-vs-exponential

+readline();
console.log(
    readline().split(' ').map(Number)
    .map((n) => {
        let sum = 0; let i = 0; let logA = Math.log(n);
        do {
            sum+=Math.log(++i);
        } while (sum <= i*logA);
        return i;
    }).join(' ')
);
