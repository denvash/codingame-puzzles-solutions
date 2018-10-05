// https://www.codingame.com/ide/puzzle/micro-assembly

let [a, b, c, d] = readline().split(' ').map(Number);

const prog = [...new Array(+readline())].map(() => {
    const [com, op1, op2, op3] = readline().split(' ');
    return {com, op1, op2, op3};
});

for (let i = 0; i < prog.length; i++) {
    const inst = prog[i];
    switch (inst.com) {
        case 'MOV': eval(`${inst.op1}=${inst.op2}`); break;
        case 'ADD': eval(`${inst.op1}=(${inst.op2})+(${inst.op3})`); break;
        case 'SUB': eval(`${inst.op1}=(${inst.op2})-(${inst.op3})`); break;
        case 'JNE':
            if (eval(inst.op2) !== eval(inst.op3)) i = eval(inst.op1) -1;
    }
}

console.log(a, b, c, d);
