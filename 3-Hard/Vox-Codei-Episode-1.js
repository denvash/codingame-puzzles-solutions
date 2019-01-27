// https://www.codingame.com/training/hard/vox-codei-episode-1

/* Helper  */ fReadInts = _ =>
  readline()
    .split(' ')
    .map(e => +e);
/* Timer   */ iTimer = 3; // Turns before a bomb explodes

/* Pattern */ afImpacts = [
  [[0, 0]], // Cross center
  [[0, -1], [0, -2], [0, -3]], // Cross up
  [[-1, 0], [-2, 0], [-3, 0]], // Cross left
  [[+1, 0], [+2, 0], [+3, 0]], // Cross right
  [[0, +1], [0, +2], [0, +3]]
]; // Cross down

/* Clipper */ fClip = (x, y) => {
  return y > -1 && y < iH && x > -1 && x < iW ? asMap[y][x] : '';
};

/* Score   */ fScore = (x, y) => {
  /* Checkpnt */ var iScore = 0;
  for (var aaBranch of afImpacts)
    for (var aiTile of aaBranch) {
      if ((s = fClip(x + aiTile[0], y + aiTile[1])) === '#') break;
      iScore += s === '@' ? 1 : 0;
    }
  /* Passives */ if (iScore > 2)
    iScore +=
      +(fClip(x + 1, y + 1) == '#') * 3 +
      (fClip(x + 1, y - 1) == '#') * 3 +
      (fClip(x - 1, y + 1) == '#') * 3 +
      (fClip(x - 1, y - 1) == '#') * 3;
  return iScore;
}; // <= I know... not the determinist way to do it!

/* Explode */ fAction = (x, y) => {
  /* Elapsing */ asMap.map(i => i.map((a, b, c) => (a > 0 ? c[b]-- : a)));
  printErr(asMap.join('\n'));
  /* Set Wait */ if (!!~'12'.indexOf(asMap[y][x])) print('WAIT');
  /* Set Time */ else {
    for (var aaBranch of afImpacts)
      for (var aiTile of aaBranch) {
        if ((s = fClip(x + aiTile[0], y + aiTile[1])) === '#') break;
        if (s === '@') asMap[y + aiTile[1]][x + aiTile[0]] = '3';
      }
    print(iXmax + ' ' + iYmax);
  }
};

[iW, iH] = fReadInts();
var asMap = [...Array(iH)].map(e => readline().split(''));

while (true) {
  var iMax = 0,
    iXmax,
    iYmax,
    aiScores = [];

  /* Input Reads */ [iR, iB] = fReadInts();
  /* Brute Force */ var iY = iH;
  while (iY-- > 0) {
    var iX = iW;
    while (iX-- > 0) {
      i = fScore(iX, iY);
      aiScores.push([i, iX, iY]);
      if (i > iMax && ~'.123'.indexOf(asMap[iY][iX])) {
        iMax = i;
        iXmax = iX;
        iYmax = iY;
      }
    }
  }
  /* Fishy Score!*/ aiScores.sort((a, b) => a[0] < b[0]);
  if (aiScores[0][0] > aiScores[1][0] * 2) {
    iXmax = aiScores[1][1];
    iYmax = aiScores[1][2];
  }
  /* Chain Bombs */ fAction(iXmax, iYmax);
}
