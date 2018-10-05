// https://www.codingame.com/ide/puzzle/aneo

const maxSpeed = +readline();

const lightsTraffic = [...new Array(+readline())].map(() => {
    const [distance, duration] = readline().split(' ');
    return {distance: +distance, duration: +duration};
});

// For each speed, check if the car passes all lights in time.
for (let speed = maxSpeed; speed >= 0; speed--) {
    if (lightsTraffic.every((light) => {
        const travelTime = light.distance / (speed / 3.6);
        const travelTimeRounded = parseFloat(Math.round(travelTime));
        // Using delta because of float precision (23.9999 is 30)
        const isRounded = travelTimeRounded - travelTime < 0.01;
        const totalTravelTime = isRounded ? travelTimeRounded : travelTime;
        // Green light is even number
        return Math.floor(totalTravelTime / light.duration) % 2 === 0;
    }) ) {
        console.log(speed);
        break;
    }
}
