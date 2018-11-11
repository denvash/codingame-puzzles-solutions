// https://www.codingame.com/ide/puzzle/brackets-enhanced-edition

const exprs = [...new Array(+readline())].map(() => readline().split(''));

const isValidExpr = (stack, b) => {
    switch (b) {
        case '(': case ')':
            if (stack[stack.length-1] === '(') stack.pop(); else stack.push('(');
            break;
        case '[': case ']':
            if (stack[stack.length-1] === '[') stack.pop(); else stack.push('[');
            break;
        case '<': case '>':
            if (stack[stack.length-1] === '<') stack.pop(); else stack.push('<');
            break;
        case '{': case '}':
            if (stack[stack.length-1] === '{') stack.pop(); else stack.push('{');
            break;
    }
    return stack;
};

exprs.forEach((expr) => {
    console.log(expr.reduce(isValidExpr, []).length === 0);
});

