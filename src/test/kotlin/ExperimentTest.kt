import easy.gravityTumbler.printNChar
import org.junit.Test

class ExperimentTest {

    @Test
    fun asIteration() {
        (0 until 10).asIterable().forEach { println("It's the $it iteration") }
    }

    @Test
    fun iterable() {
        (0 until 10).forEach {
            run {
                println("It's the $it iteration")
            }
        }
    }

    @Test
    fun printNChars() {
        printNChar(5, '#')
    }

    @Test
    fun countCharsInInput() {
        println(".....####...###...#".count { it == '#' }) //8
    }

    @Test
    fun toLongEquality() {
        println("'4' as Long = " + '4'.toLong())
        println("\"4\" as Long = " + "4".toLong())
        println("\"42\" as Long = " + "42".toLong())

        assert('4'.toString().toLong() == 4.toLong())
    }

    @Test
    fun findGridFactor() {
        val decimalNumber = 14
        var factor = 0.0
        while (Math.pow(3.toDouble(), factor++) < decimalNumber) {
        }
        assert(factor.toInt() == 4)
    }

    @Test
    fun indexInGrid() {
        var minValue = 0
        for (factor in 0..2) {
            minValue += Math.pow(3.toDouble(), factor.toDouble()).toInt()
        }
        minValue *= (-1)
        assert(8 == (-5) - minValue)
    }

    @Test
    fun toBT() {
        val n = 8
        var str = when {
            n % 3 == 0 -> "-1"
            n % 3 == 1 -> "0"
            else -> "1"
        }

        for (i in 1..2) {
            val factored = Math.pow(3.toDouble(), i.toDouble()).toInt()
            var count = 1
            while (n - factored*count++ >= 0) {}
            --count

            val digit = when {
                count % 3 == 1 -> "-1"
                count % 3 == 2 -> "0"
                else -> "1"
            }

            str = "$digit$str"
        }
        assert("T11" == str.replace("-1", "T", true))
    }
}