// https://www.codingame.com/training/easy/darts

const R = +readline() / 2;
const playersScores =
    new Map([...Array(+readline())].map(() => [readline(), 0]));

const throws = [...Array(+readline())].map(() => {
    const [name, x, y] = readline().split(' ');
    return [name, +x, +y];
});

const DIAMOND_SCORE = 15; const CIRCLE_SCORE = 10; const SQUARE_SCORE = 5;
throws.forEach(([player, x, y]) => {
    const currentScore = playersScores.get(player);
    if (Math.abs(x) + Math.abs(y) <= R) {
        playersScores.set(player, currentScore+DIAMOND_SCORE);
    } else if (Math.sqrt(x*x + y*y) <= R) {
        playersScores.set(player, currentScore+CIRCLE_SCORE);
    } else if (Math.max(x, y) <= R && Math.min(x, y) >= -R) {
        playersScores.set(player, currentScore+SQUARE_SCORE);
    }
});

Array.from(playersScores)
    .sort(([, score1], [, score2]) => score2-score1)
    .forEach(([name, score]) => console.log(name, score));

