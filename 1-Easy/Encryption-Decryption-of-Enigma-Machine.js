// https://www.codingame.com/ide/puzzle/encryptiondecryption-of-enigma-machine

const [op, N] = [readline(), +readline()];
const rotor = [...Array(3)].map(() => readline().split(''));
const message = readline().split('');

const ascii = (a) => a.charCodeAt(0);
const toIndex = (c) => (ascii(c) - ascii('A')) % 26;
const toChar = (c) => String.fromCharCode(c + ascii('A'));

let encode = message.map(
    (c,i) => toIndex(String.fromCharCode((ascii(c) + N + i)))
);
rotor.forEach((r) => encode = encode.map((c) => toIndex(r[c])));

const rotorStep = (i) => (c) => toChar(rotor[i].indexOf(c));
const decode = message.map(rotorStep(2)).map(rotorStep(1)).map(rotorStep(0))
    .map(
        (c,i) => {
            const idx = (toIndex(c) - N - i) % 26;
            return toIndex(toChar(idx > 0 ? idx : 26 + idx));
        }
    );

const stickChars = (a) => a.map(toChar).join('');
console.log(op === 'DECODE' ? stickChars(decode) : stickChars(encode));
