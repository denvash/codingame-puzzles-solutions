// https://www.codingame.com/training/easy/hunger-games

const players = [...Array(+readline())].map(readline);

const turns = [...Array(+readline())].map(() => {
  const [killer, , ...killed] = readline()
    .replace(/,/g, '')
    .split(/\s+/);

  return { killer, killed };
});

players.sort().forEach((player, i) => {
  const killed = turns
    .filter(turn => turn.killer === player)
    .map(turn => turn.killed)
    .flat();

  const killer = turns
    .filter(turn => turn.killed.includes(player))
    .map(turn => turn.killer)
    .pop();

  console.log(`Name: ${player}`);
  console.log(
    `Killed: ${killed.length === 0 ? 'None' : killed.sort().join(', ')}`
  );

  console.log(`Killer: ${killer === undefined ? 'Winner' : killer}`);
  if (i !== players.length - 1) console.log();
});
