// https://www.codingame.com/ide/puzzle/isbn-check-digit

const invalidISBNS = []
while (+readline() -- > 0) {
  const ISBN = readline()

  // Match valid ISBNS - https://regex101.com/r/KHZYRx/4
  if (ISBN.length != 10 && ISBN.length != 13 || !ISBN.match(/\d{12}[X\d]|\d{9}[X\d]/g) || !isValidCheckDigit(ISBN)) {
    invalidISBNS.push(ISBN)
  }
}

print(`${invalidISBNS.length} invalid:`)
invalidISBNS.forEach((invalidISBN) => print(invalidISBN))

function isValidCheckDigit (ISBN) {
  let sum = 0
  const length = ISBN.length
  for (let i = 0; i < length - 1; ++i) {
    sum += parseInt(ISBN[i]) * ((length == 10) ? 10 - i : (i % 2 === 0 ? 1 : 3))
  }
  const mod = length == 10 ? 11 : 10
  const last = ISBN[length - 1] == 'X' ? 10 : ISBN[length - 1]
  return last == (mod - sum % mod) % mod
}
