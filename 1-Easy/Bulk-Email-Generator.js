// https://www.codingame.com/ide/puzzle/bulk-email-generator

let N = parseInt(readline());
let clauseCounter = 0;

let text = '';
while (N-- > 0) {
    text = text.concat(readline() + '\n');
}

// https://regex101.com/r/FSGnVD/1
let scheme = text.match(/\(.*?[^]*?\)/g);

scheme.forEach(clause => {
    const splittedChoices = clause
        .replace('(', '')
        .replace(')', '')
        .split(/[|]/g);
    const choice = splittedChoices[clauseCounter++ % splittedChoices.length];
    text = text.replace(clause, choice);
});

print(text);

/* one liner : 
l=readline
print([...Array(+l(x=0))].map(l).join``.replace(/\([^\)]*\)/g,a=>(p=a.slice(1,-1).split`|`)[x++%p.length]))
*/
