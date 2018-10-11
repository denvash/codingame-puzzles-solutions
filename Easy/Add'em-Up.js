// https://www.codingame.com/ide/puzzle/addem-up

const [, numbers] = [readline(), readline().split(' ').map(Number)];

const getCost = (nums) => {
    let cost = 0;
    while (nums.length > 1) {
        nums.sort((a, b) => b-a);
        const sumOfMin = nums.pop() + nums.pop();
        nums.push(sumOfMin);
        cost+=sumOfMin;
    }
    return cost;
};

console.log(getCost(numbers));
