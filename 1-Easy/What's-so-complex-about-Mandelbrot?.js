// https://www.codingame.com/ide/puzzle/whats-so-complex-about-mandelbrot

const [real, img] = readline().match(/[-+]?[0-9]*\.?[0-9]+/g).map(Number);
const m = +readline();

class Complex {
    constructor(re, im) {
        this.re = re;
        this.im = im;
    }
}

const ComplexBuilder = (re, im) => new Complex(re, im);
const plus = (c1, c2) => ComplexBuilder(c1.re + c2.re, c1.im + c2.im);
const multiply = (c1) => ComplexBuilder(c1.re ** 2 - c1.im ** 2, 2 * c1.re * c1.im);
const abs = (c1) => Math.sqrt(c1.re ** 2 + c1.im ** 2);
const f = (n) => n <= 0 ? ComplexBuilder(0, 0) : plus(multiply(f(n - 1)), ComplexBuilder(real, img));

for (var i = 0; i < m; i++) {
    if (abs(f(i)) > 2) {
        break;
    }
}
print(i);