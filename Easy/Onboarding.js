// https://www.codingame.com/ide/puzzle/onboarding

while (true) {
    const enemy1 = readline();
    const dist1 = +readline();
    const enemy2 = readline();
    const dist2 = +readline();

    console.log(dist1 <= dist2 ? enemy1 : enemy2);
}
