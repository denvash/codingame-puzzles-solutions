// https://www.codingame.com/ide/puzzle/self-driving-car-testing

const CAR = '#';

r = readline;
s = _ => r().split(';');
let N = +r();
const [a, ...str] = s();

let position = +a;
// directions = [[10,R],[4,L],[8,S]...]
const directions = str.map(s => [+s.slice(0, -1), s[s.length - 1]]);

for (let i = 0; N > 0 && i < directions.length; N--) {
    let [numOfDuplicates, road] = s();

    while (numOfDuplicates-- > 0) {

        // as string.replaceAt(position)
        print(road.substr(0, position - 1) + CAR + road.substr(position));
        if (--directions[i][0] <= 0) {
            i++;
        }
        if (i >= directions.length) {
            break;
        }
        position += directions[i][1] == 'L' ? -1 : directions[i][1] == 'R' ? 1 : 0;
    }
}