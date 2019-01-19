// https://www.codingame.com/ide/puzzle/addem-up

const [, numbers] = [readline(), readline().split(' ').map(Number)];

const getCost = (numbers) => {
    let cost = 0;
    while (numbers.length > 1) {
        numbers.sort((a, b) => b-a);
        const sumOfMin = numbers.pop() + numbers.pop();
        numbers.push(sumOfMin);
        cost+=sumOfMin;
    }
    return cost;
};

console.log(getCost(numbers));
