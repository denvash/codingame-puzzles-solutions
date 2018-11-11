// https://www.codingame.com/ide/puzzle/disordered-first-contact

const iterateMsg = action => {
    for (let start = 0, length = 1; start < msg.length; start += length++) {
        let substr = msg.substr(start, length);
        action(length, substr);
    }
};

const encode = _ => {
    let transformed = '';
    iterateMsg(
        (length, substr) =>
            (transformed =
                length % 2 === 0 ? substr + transformed : transformed + substr)
    );
    return transformed;
};

// Example for decoding: ghibcadef
// 1.ghi 2.def->ghi 3. bc->defghi 4. a->bcdefghi
const decode = msg => {
    let times, lastLength;
    iterateMsg((length, substr) => {
        times = length;
        lastLength = substr.length;
    });

    // Initiate transformed from with LastLength
    isLastAddedFromLeft = times % 2 === 0;
    let strRef = {
        transformed: isLastAddedFromLeft
            ? msg.substr(0, lastLength)
            : msg.slice(-lastLength)
    };
    msg = isLastAddedFromLeft
        ? msg.slice(lastLength)
        : msg.slice(0, -lastLength);
    decodeAux(strRef, msg, times - 1, !isLastAddedFromLeft);
    return strRef.transformed;
};

const decodeAux = (strRef, msg, times, addFromLeft) => {
    if (times === 0) {
        return;
    }
    strRef.transformed = addFromLeft
        ? msg.substr(0, times) + strRef.transformed
        : msg.slice(-times) + strRef.transformed;
    msg = addFromLeft ? msg.slice(times) : msg.slice(0, -times);
    decodeAux(strRef, msg, times - 1, !addFromLeft);
};

const times = +readline();
let timesTrans = Math.abs(times);
let msg = readline();

while (timesTrans--) {
    if (times < 0) {
        msg = encode();
    } else {
        msg = decode(msg);
    }
}

print(msg);
