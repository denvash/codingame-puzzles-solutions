// https://www.codingame.com/ide/puzzle/gravity-centrifuge-tuning

import java.math.BigInteger
import java.util.*

fun fibonacci(): Sequence<Pair<BigInteger, BigInteger>> =
        generateSequence(Pair(BigInteger("0"), BigInteger("1"))) { Pair(it.second, it.first + it.second) }

fun tumblerSeq(n : BigInteger): Sequence<BigInteger> {
    val firstPair = fibonacci().find { it.second >= n }
    return generateSequence(firstPair) { Pair(it.second - it.first , it.first)}.map { it.second }
}

fun main(args: Array<String>) {
    var n = BigInteger(Scanner(System.`in`).next())
    var binary = ""

    for(num in tumblerSeq(n)) {
        binary += if (n >= num) "1" else "0"
        if (n>=num) n-=num
        if ( num <= BigInteger("1") ) break
    }
    println(BigInteger(binary,2).toString(8))
}