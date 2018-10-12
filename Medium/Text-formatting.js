// https://www.codingame.com/ide/puzzle/text-formatting

console.log(readline().toLowerCase()
    .replace(/(\s+)?([,.;])(\s+)?/g, '$2') // single space
    .replace(/(([,.;])\2*)/g, '$2') // single punctuation
    .replace(/([,.;])/g, '$1 ') // single space after punctuation
    .replace(/(^|\. )(.)/g, (c) => c.toUpperCase()) // upper case
    .trim()); // trim whitespace before and after sentence
