// https://www.codingame.com/ide/puzzle/jack-silver-the-casino

let [ROUNDS,CASH] = [+readline(),+readline()];
const win = (n) => CASH+=(n*Math.ceil(CASH/4));
const lose = (n) => CASH-=(n*Math.ceil(CASH/4));

while(ROUNDS-- > 0) {
    const [ball,action,number] = readline().split(' ');
    switch(action) {
        case 'PLAIN':
            ball === number ? win(35) : lose(1); 
            break;
        case 'EVEN':
            ball%2 === 0 && ball != 0 ? win(1) : lose(1);
            break;
        default:
            ball%2 === 1 ? win(1) : lose(1); 
    }
}
print(CASH);