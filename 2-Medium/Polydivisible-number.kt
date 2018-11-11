// https://www.codingame.com/ide/puzzle/polydivisible-number

import java.math.BigInteger
import java.util.*

// Migrate BigInteger from java lib, because the IDE not supporting kotlin's BigInteger.
fun Int.toBigInteger(): BigInteger = BigInteger.valueOf(this.toLong())
fun String.toBigInteger(): java.math.BigInteger = java.math.BigInteger(this)

fun main(args: Array<String>) {
    val s = Scanner(System.`in`).nextLine().split(" ").map { it.toBigInteger() }.reversed()

    for (base in 2 until 36) {
        var base10 = BigInteger.valueOf(0)!!
        if (!s.all { it < base.toBigInteger() }) continue

        for (k in 0 until s.size) {
            var tempBase = 1.toBigInteger()

            // Using Math.pow with converting do double losing precision, so repeat is the best solution.
            repeat(k) { tempBase *= base.toBigInteger() }
            val currNum = s[k].times(tempBase)
            base10 += currNum
        }

        var flag = true
        for (n in 0 until base10.toString().length) {
            val current = base10.toString().slice(0..n).toBigInteger()
            if (current.rem((n + 1).toBigInteger()) != 0.toBigInteger()) {
                flag = false
                break
            }
        }
        if (flag) println(base)
    }
}