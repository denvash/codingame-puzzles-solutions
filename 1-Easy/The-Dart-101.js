// https://www.codingame.com/training/easy/the-dart-101

const players = [...Array(+readline())].map(() => {
  const name = readline();
  return { name, stats: {}, shots: [] };
});

const MISSED = 0;
const THROWS_MAX = 3;
const SCORE_TARGET = 101;
const SCORE_FIRST_MISS = 20;
const SCORE_SECOND_MISS = 30;
const SCORE_THIRD_MISS = 0;

players.forEach(
  player =>
    (player.shots = readline()
      .split(' ')
      .map(score => (score === 'X' ? MISSED : eval(score))))
);

players.forEach(
  player =>
    (player.stats = player.shots.reduce(
      (stats, currScore) => {
        if (stats.throws === THROWS_MAX) {
          stats.rounds++;
          stats.lastRoundScore = stats.score;
          stats.throws = 0;
          stats.missStreak = 0;
        }
        stats.throws++;
        if (currScore === MISSED) {
          stats.missStreak++;
          stats.score =
            stats.missStreak === 1
              ? stats.score - SCORE_FIRST_MISS
              : stats.missStreak === 2
              ? stats.score - SCORE_SECOND_MISS
              : 0;
          // No negative score
          stats.score = stats.score < 0 ? 0 : stats.score;
        } else {
          if (stats.score + currScore > SCORE_TARGET) {
            stats.score = stats.lastRoundScore;
            stats.throws = THROWS_MAX;
          } else {
            stats.missStreak = 0;
            stats.score += currScore;
          }
        }
        return stats;
      },
      {
        score: 0,
        lastRoundScore: 0,
        throws: 0,
        missStreak: 0,
        rounds: 0
      }
    ))
);

console.log(
  players
    .filter(player => player.stats.score === SCORE_TARGET)
    .reduce((bestPlayer, currPlayer) => {
      if (
        (currPlayer.stats.rounds == bestPlayer.stats.rounds &&
          currPlayer.stats.throws < bestPlayer.stats.throws) ||
        currPlayer.stats.rounds < bestPlayer.stats.rounds
      ) {
        return currPlayer;
      }
      return bestPlayer;
    }).name
);
