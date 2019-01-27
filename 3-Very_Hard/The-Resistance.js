// https://www.codingame.com/training/expert/the-resistance

const code = {
  'A': '.-',
  'B': '-...',
  'C': '-.-.',
  'D': '-..',
  'E': '.',
  'F': '..-.',
  'G': '--.',
  'H': '....',
  'I': '..',
  'J': '.---',
  'K': '-.-',
  'L': '.-..',
  'M': '--',
  'N': '-.',
  'O': '---',
  'P': '.--.',
  'Q': '--.-',
  'R': '.-.',
  'S': '...',
  'T': '-',
  'U': '..-',
  'V': '...-',
  'W': '.--',
  'X': '-..-',
  'Y': '-.--',
  'Z': '--..'
};

const L = readline();
const W = [...Array(+readline())].map(() => readline().split('').map(x => code[x]).join(''));

const c = Array(L.length + 1).fill(0);
c[L.length] = 1;

for (let i = L.length - 1; i >= 0; i--) {
  c[i] = W.reduce((prev , w) => prev + (L.startsWith(w, i) ? (c[i + w.length] || 0) : 0), 0);
}

console.log(c[0]);