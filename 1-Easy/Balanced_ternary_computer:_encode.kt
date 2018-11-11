package easy.balancedTernary

import java.util.*

// https://www.codingame.com/ide/puzzle/balanced-ternary-computer-encode

const val MINUS_SIGN = "T"
const val BT_BASE = 3

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val decimalNumber = input.nextInt()

    // decimalNumber.absoluteValue not supported in this puzzle
    val absoluteValue = if (decimalNumber < 0) (-1) * decimalNumber else decimalNumber

    val gridSize = getTruthTableSize(absoluteValue)
    println(gridSize.getIndexInTruthTable(decimalNumber).toBT(gridSize))

}

/**
 * Find the BT (Balanced Ternary) truth table size
 */
fun getTruthTableSize(target: Int): Int {
    var factor = 0.0
    while (Math.pow(BT_BASE.toDouble(), factor++) < target) {
    }
    return factor.toInt()
}

/**
 * Get index of BT truth table by it's tableValue
 */
private fun Int.getIndexInTruthTable(tableValue: Int): Int {
    var minValue = 0
    for (factor in 0..this) {
        minValue += Math.pow(BT_BASE.toDouble(), factor.toDouble()).toInt()
    }
    minValue *= (-1)
    return tableValue - minValue
}

/**
 * Generates BT string from it's truth table (by it's size)
 */
private fun Int.toBT(tableSize: Int): String {

    // 3^0 value
    var str = when {
        this % BT_BASE == 0 -> "-1"
        this % BT_BASE == 1 -> "0"
        else -> "1"
    }

    // 3^1 until 3^tableSize
    for (i in 1 until tableSize) {
        val factored = Math.pow(3.toDouble(), i.toDouble()).toInt()
        var count = 1
        while (this - factored * count++ >= 0) {
        }

        // Remove extra iteration
        --count

        val digit = when {
            count % BT_BASE == 1 -> "-1"
            count % BT_BASE == 2 -> "0"
            else -> "1"
        }

        str = "$digit$str"
    }

    // Drop leading '0', special case if str="0".
    // Replace -1 to T.
    return if (str.length != 1) str.dropWhile { it == '0' }.replace("-1", MINUS_SIGN, true) else str
}
