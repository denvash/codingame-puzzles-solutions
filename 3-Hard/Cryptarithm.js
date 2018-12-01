// https://www.codingame.com/training/hard/cryptarithm

/**
 * Generates all permutations of a set.
 * @param {Array|String} arr - The set of elements.
 * @param {Number} size - Number of elements to choose from the set.
 */
function* permutation(arr, size = arr.length) {
    const data = [];
    const indicesUsed = [];
    yield* permutationUtil(0);

    /**
     * @param {Number} index of permutation
     * @return {Generator}
     */
    function* permutationUtil(index) {
        if (index === size) {
            return yield this.clones ? data.slice() : data;
        }
        for (let i = 0; i < arr.length; i++) {
            if (!indicesUsed[i]) {
                indicesUsed[i] = true;
                data[index] = arr[i];
                yield* permutationUtil(index + 1);
                indicesUsed[i] = false;
            }
        }
    }
}

// We generate a permutation and checking if the sum is valid
const r = () => readline().split('');
const words = [...Array(+readline())].map(() => r());
const wordsTotal = r();

const dictionary = [...new Set([...words, wordsTotal].flat())].sort();

const permutationGenerator =
    permutation([...Array(10).keys()], dictionary.length);

for (let permutation of permutationGenerator) {
    const keyValueArray = dictionary.map((v, i) => [v, permutation[i]]);
    const cryptMap = new Map(keyValueArray);
    if (cryptMap.get(wordsTotal[0]) === 0) continue;

    const wordToNumber =
        (arr) => +arr.map((char) => cryptMap.get(char)).join('');

    const sumWords =
        words.map((word) => wordToNumber(word))
            .reduce((sum, curr) => sum+=curr);

    const sumTotal = wordToNumber(wordsTotal);

    if (sumWords === sumTotal) {
        cryptMap.forEach((value, key) => console.log(key, value));
        break;
    }
}
