import java.util.*

// https://www.codingame.com/ide/puzzle/the-river-i-

fun main(args : Array<String>) {
    assert(48.toLong() == 42.toLong().nextRiver())
    val input = Scanner(System.`in`)
    var r1 = input.nextLong()
    var r2 = input.nextLong()

    while (r1 != r2) {
        when {
            r1 < r2 -> r1 = r1.nextRiver()
            else -> r2 = r2.nextRiver()
        }
    }
    println(r1)

}

private fun Long.nextRiver(): Long {
    var n = this
    this.toString().forEach { n += it.toString().toLong() }
    return n
}