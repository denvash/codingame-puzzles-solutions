// https://www.codingame.com/ide/puzzle/111-rubiks-cube-movements

function flatten(arr) {
    return Array.prototype.concat(...arr);
}

const COUNTER_CLOCKWISE = "'";
const LOCK_R_L = 'x';
const LOCK_U_D = 'y';
// not used const LOCK_F_B = 'z';

const [rotations, f1, f2] = [readline().split(' '), readline(), readline()];
const rubik = [['R', 'L'], ['U', 'D'], ['F', 'B']];

// Make swaps according to Rotation notations.
const rotate = rotation => {

    // rotation[0] = Direction,
    // rotation[1] = clokwise or counter-clockwise;
    let [[x, y], k] =
        rotation[0] === LOCK_R_L
            ? [[1, 2], 1]
            : rotation[0] === LOCK_U_D 
                ? [[0, 2], 2]
                : [[0, 1], 0];
    if (rotation[1] === COUNTER_CLOCKWISE) {
        k = rotation[0] === LOCK_R_L ? 2 : rotation[0] == LOCK_U_D ? 0 : 1;
    }
    const [X, Y] = rubik[k];
    rubik[k] = [Y, X];
    [rubik[x], rubik[y]] = [rubik[y], rubik[x]];
};

rotations.forEach(rotation => {
    rotate(rotation);
});

const defaultRubik = [['R', 'L'], ['U', 'D'], ['F', 'B']];
const findFace = (face) => flatten(defaultRubik)[flatten(rubik).indexOf(face)];
print(findFace(f1));
print(findFace(f2));
