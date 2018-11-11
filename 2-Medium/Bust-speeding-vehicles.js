// https://www.codingame.com/ide/puzzle/bust-speeding-vehicles

const L = +readline();
let N = +readline();
const cars = [];
while (N--) {
    const [plate, d, t] = readline().split(' ');
    cars[plate] = cars[plate] || [];
    cars[plate].push({distance: +d, time: +t});
}

let flag = false;

Object.keys(cars).forEach(
    (plate) => {
        const speeds = cars[plate].reduce((speeds, record, i) => {
            if (i < 1) return speeds;
            const prevRecord = cars[plate][i-1];
            const t = (record.time - prevRecord.time) / 3600;
            const d = record.distance - prevRecord.distance;
            speeds.push({distance: record.distance, speed: d/t});
            return speeds;
        }, []);
        speeds.forEach((record) => {
            if (record.speed > L) {
                console.log(plate, record.distance);
                flag=true;
            }
        });
    }
);

if (flag === false) console.log('OK');
