// https://www.codingame.com/ide/puzzle/hidden-word

const dict = [...new Array(+readline())].map(readline);
const grid = [...new Array(+(readline().split(' ')[0]))]
    .map(() => readline().split(''));
let mark = [...grid];

const horizontalWords =
    (grid) => [...new Array(grid.length)].map((_, i) => grid[i].join(''));

const horz = horizontalWords(grid);

const markWords = (words, action, grid) => {
    words.forEach((word, wordIndex) => {
        dict.forEach((dictWord) => {
            action(word, wordIndex, dictWord, grid);
        });
    });
};

const reverse = (str) => str.split('').reverse().join('');
const indexOf = (word, dictWord) => word.indexOf(dictWord) === -1 ?
    word.indexOf(reverse(dictWord)) : word.indexOf(dictWord);

const markHorizontal = (word, wordIndex, dictWord, mark) => {
    if (word.includes(dictWord) ||
        word.includes(reverse(dictWord))) {
        let index = indexOf(word, dictWord);
        mark[wordIndex] = (mark[wordIndex].join('').substring(0, index) +
                'x'.repeat(dictWord.length) + mark[wordIndex]
                .join('')
                .substring(index + dictWord.length))
            .split('');
    }
};

markWords(horz, markHorizontal, mark);

const mirrorLines = (grid) => {
    const swapped = [];
    for (let col = 0; col < grid.length; col++) {
        let word = '';
        for (let row = 0; row < grid.length; row++) {
            word += grid[row][col];
        }
        swapped.push(word.split(''));
    }
    return swapped;
};

const gridMirrorLines = mirrorLines(grid);
const verticalWords = horizontalWords(gridMirrorLines);
const markMirrorLines = mirrorLines(mark);

markWords(verticalWords, markHorizontal, markMirrorLines);
mark = mirrorLines(markMirrorLines);

// get all diagonal words from left to right
const diagLR = (grid) => {
    const diagXY = [];
    for (let row = 0; row < grid.length - 1; row++) {
        let word = '';
        let word2 = '';
        for (let k = 0; k < grid.length && row + k < grid.length; k++) {
            word += grid[row + k][k];
            word2 += grid[k][row + k];
        }
        diagXY.push([row, word]);
        diagXY.push([row, word2]);
    }
    diagXY.shift();
    return diagXY;
};

const diagXY = diagLR(grid);

// mark all matched words from left to right
const markDiaognal = (word, wordIndex, dictWord, mark) => {
    if (word[1].includes(dictWord) ||
        word[1].includes(reverse(dictWord))) {
        let index = indexOf(word[1], dictWord);
        if (wordIndex % 2 === 1) {
            for (let k = 0; k < dictWord.length; k++) {
                mark[word[0] + k][k] = 'x';
            }
        } else {
            for (let k = 0; k < dictWord.length; k++) {
                mark[k + index][word[0] + k + index] = 'x';
            }
        }
    }
};
markWords(diagXY, markDiaognal, mark);

const mirrorDiag =
    (grid) => horizontalWords(grid).map((line) => reverse(line).split(''));

const gridMirrorDiag = mirrorDiag(grid);
const diagYX = diagLR(gridMirrorDiag);
const markMirrorDiag = mirrorDiag(mark);
markWords(diagYX, markDiaognal, markMirrorDiag);
mark = mirrorDiag(markMirrorDiag);

console.log(
    mark.reduce(
        (answer, wordArray) =>
            answer += wordArray.filter((e) => e !== 'x').join(''),
        '')
);
