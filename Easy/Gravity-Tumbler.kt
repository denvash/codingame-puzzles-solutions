package easy.gravityTumbler

import java.util.*

// https://www.codingame.com/ide/puzzle/gravity-tumbler

enum class Puzzle(val char: Char) {
    HEAVY_BIT('#'),
    EMPTY_BIT('.'),
}

/**
 * Print n-Times c-Char.
 */
fun printNChar(n: Int, c: Char) =
    run {
        (0 until n).forEach { print(c) }
    }

fun main(args: Array<String>) {

    val input = Scanner(System.`in`)
    val width = input.nextInt()
    val height = input.nextInt()

    // gravityToRightMode(true) when count is even: height lines of width characters.
    // else(false) when count is odd: width lines of height characters.
    val gravityToRightMode = when {
        input.nextInt() % 2 == 0 -> true
        else -> false
    }

    // Trim whitespace
    if (input.hasNextLine()) {
        input.nextLine()
    }

    // for index 'i' in histogram array:
    // histogram[i] = number of '#' in line i.
    val inputHistogram = IntArray(height) { input.nextLine().count { it == '#' } }

    // easy.gravityTumbler.gravityTumbler.transform input array (inputHistogram) according to input mode.
    val transformed = when {
        gravityToRightMode -> inputHistogram
        else -> transform(array = inputHistogram, transformedSize = width)
    }

    // Print with applying the gravity mode
    transformed.forEach {
        printNChar(
            when {
                gravityToRightMode -> width
                else -> height
            } - it, Puzzle.EMPTY_BIT.char
        )
        printNChar(it, Puzzle.HEAVY_BIT.char)
        println()
    }
}

/**
 * easy.gravityTumbler.gravityTumbler.transform L lines R rows to array of R line L rows,
 * transformation according to puzzle rules,
 * which means the '#' will "fall" by gravity.
 */
fun transform(array: IntArray, transformedSize: Int): IntArray {
    val transformed = IntArray(transformedSize)

    for (i in array.indices.reversed()) {
        for (j in transformed.indices.reversed()) {
            if (array[i] == 0) break
            ++transformed[j]
            --array[i]
        }
    }
    return transformed
}
