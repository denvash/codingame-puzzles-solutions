// https://www.codingame.com/ide/puzzle/simple-awale

readArrayNumber = _ => readline().split(' ').map(Number);
let i = +readline(myBowls = readArrayNumber(opBowls = readArrayNumber()));

const my = myBowls;
const op = opBowls;

const size = my.length;
let N = my[i];

for (my[i++] = 0; N-- > 0; i = i % (2*size-1)) {
    i < size ? my[i++]++ : op[i++ - size]++
}

replacer = arr => arr.join(' ').replace(/(\d)$/, '[$1]');
print(replacer(op));
print(replacer(my));
if (i == my.length) print('REPLAY');