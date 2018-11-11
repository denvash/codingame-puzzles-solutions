// https://www.codingame.com/ide/14434681a313ee6c744c53c236c34b25652ac323

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
