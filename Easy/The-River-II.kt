package easy.theRiverK2

import java.util.*

// https://www.codingame.com/ide/puzzle/the-river-ii-

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val r = input.nextLong()

    var answer = "NO"

    for (k in 0..r) {
        if (k.nextRiver() == r) answer = "YES"
    }
    println(answer)
}

private fun Long.nextRiver(): Long {
    var n = this
    this.toString().forEach { n += it.toString().toLong() }
    return n
}