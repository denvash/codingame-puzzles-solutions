// https://www.codingame.com/ide/puzzle/may-the-triforce-be-with-you

const N = +readline();
const r = (c, n) => c.repeat(n);

print('.' + r(' ', 2 * N - 2) + r('*', 1));

const top = (n) => {
    for (let i = 1; i < n; i++) {
        let stars = r('*', 2 * i + 1);
        let spaces = r(' ', 2 * n - i - 1);
        print(spaces + stars);
    }
};

const buttom = (n) => {
    for (let i = 0; i < n; i++) {
        let stars = r('*', 2 * i + 1);
        let spaces = r(' ', n - i - 1);
        print(spaces + stars + spaces + ' ' + spaces + stars);
    }
};

top(N);
buttom(N);