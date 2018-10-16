// https://www.codingame.com/ide/puzzle/next-car-license-plate

const zeroFill = (number, width) => {
    width -= number.toString().length;
    return width > 0 ? Array(width + (/\./.test(number) ? 2 : 1)).join('0') + number : number;
};

const ascii = (a) => a.charCodeAt(0);
const toIndex = (c) => (ascii(c) - ascii('A')) % 26;
const toChar = (c) => String.fromCharCode(c + ascii('A'));

let x = readline().split('-').map((p) => p.split(''));
let n = +readline();
let [hours, mins, secs] = [x[0].map(toIndex), x[2].map(toIndex), +x[1].join('')];

const [HOURS,MIN,SEC] = ['HOURS','MIN','SECONDS'];
let state = SEC;

// We turned the plates to timer with easy states.
while (n > 0) {
    if (state === SEC) {
        if (secs + n <= 999) {
            secs += n;
            break;
        }
        n -= (999 - secs);
        secs = 1;
        state = MIN;
        continue;
    }
    if (state === MIN) {
        mins[1]++;
        if (mins[1] === 26) {
            mins[1] = 0;
            mins[0]++;
            if (mins[0] === 26) {
                mins[0] = 0;
                state = HOURS;
                continue;
            }
        }
        n--;
        state = SEC;
    }
    if (state === HOURS) {
        hours[1]++;
        if (hours[1] === 26) {
            hours[1] = 0;
            hours[0]++;
            if (hours[0] === 26) {
                hours[0] = 0;
            }
        }
        n--;
        state = SEC;
    }
}

console.log([hours.map(toChar).join(''), zeroFill(secs,3), mins.map(toChar).join('')].join('-'));