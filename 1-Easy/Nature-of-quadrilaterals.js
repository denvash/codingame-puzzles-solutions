// https://www.codingame.com/ide/puzzle/nature-of-quadrilaterals

const nature = (pA, pB, pC, pD) => {
    const dist = (p, q) => Math.sqrt(((p.x - q.x) ** 2) + ((p.y - q.y) ** 2));
    const AB = dist(pA, pB);
    const AC = dist(pA, pC);
    const BD = dist(pB, pD);
    const DA = dist(pD, pA);
    const BC = dist(pB, pC);
    const CD = dist(pC, pD);

    const isParallelogram = AB === CD && BC === DA ? true : false;
    const isRhombus = AB === BC && BC === CD && CD === DA ? true : false;

    // Care for floating point, shouldn't use `===`.
    const isRightTriangle = (a, b, c) => c ** 2 <= a ** 2 + b ** 2;
    const isRectangle =
        isRightTriangle(AB, BC, AC) &&
        isRightTriangle(BC, CD, BD) &&
        isRightTriangle(CD, DA, AC) &&
        isRightTriangle(AB, DA, BD) ? true : false;

    return isRhombus && isRectangle && isParallelogram ? 'square' :
        isRhombus ? 'rhombus' :
        isRectangle ? 'rectangle' :
        isParallelogram ? 'parallelogram' :
        'quadrilateral';
};

const pts = [...new Array(+readline())].map(_ => readline().split(' '));
pts.forEach(([A, xA, yA, B, xB, yB, C, xC, yC, D, xD, yD]) =>
    print(`${A}${B}${C}${D} is a ${nature(
        { x : +xA, y : +yA },
        { x : +xB, y : +yB },
        { x : +xC, y : +yC },
        { x : +xD, y : +yD })}.`)
);