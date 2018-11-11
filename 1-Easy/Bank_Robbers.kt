package easy.bankRobbers

import java.util.*

// https://www.codingame.com/ide/puzzle/bank-robbers

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val robbersTimes = LongArray(input.nextInt())

    // Always add combination time to the robbersTimes minimum.
    (0 until input.nextInt()).forEach {
        robbersTimes[robbersTimes.indexOf(robbersTimes.min()!!)] +=
                getCombinationTime(input.nextLong(), input.nextLong())
    }
    println(robbersTimes.max()!!)
}

// 10^N * 5^(C-N) == total combination time.
fun getCombinationTime(c: Long, n: Long): Long =
    Math.pow(10.toDouble(), n.toDouble()).toLong() * Math.pow(5.toDouble(), (c - n).toDouble()).toLong()
