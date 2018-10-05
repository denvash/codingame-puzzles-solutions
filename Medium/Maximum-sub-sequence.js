// https://www.codingame.com/ide/puzzle/maximum-sub-sequence

readline();
const a = readline().split(' ').map(Number);

let incArr = [];
let max = 0;

a.forEach((start, index) => {
    let a0 = start;
    const subArray = a.filter((an, i) => {
        if (i < index) return false;
        else if (an === a0) {
            a0 += 1;
            return true;
        }
        return false;
    });
    if (max === subArray.length && incArr[0] > subArray[0] ||
        max < subArray.length) {
        incArr = subArray;
        max = incArr.length;
    }
});

console.log(incArr.join(' '));
