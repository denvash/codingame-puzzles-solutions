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
}