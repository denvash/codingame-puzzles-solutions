// https://www.codingame.com/ide/puzzle/snake-encoding

const N = +readline();
let times = +readline();

let grid = [...Array(N)].map(() => readline().split(''));

while (times--) {
    const helper = [];
    const mutate = (source, action) => {
        for (let j = 0; j < grid.length; j++) {
            const temp = [];
            for (let i = 0; i < grid.length; i++) {
                temp.push(source[i][j]);
            }
            helper.push(action(j,temp));
        }
    };
    mutate(grid,(j,col) => j % 2 === 0 ? col.reverse() : col)

    const flat = helper.flat();
    flat.unshift(flat.pop());

    const answer = flat.reduce((a, c) => {
        if (a.length === 0 || a[a.length - 1].length === grid.length) a.push([c]);
        else a[a.length - 1].push(c);
        return a;
    }, []).map((a, i) => i % 2 === 0 ? a.reverse() : a);

    helper.length = 0;
    mutate(answer,(_,col) => col.join(''))
    grid = helper;
}
grid.forEach((e) => console.log(e));