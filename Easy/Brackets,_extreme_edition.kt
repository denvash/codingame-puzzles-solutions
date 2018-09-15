package easy.balanceBrackets

import java.util.*

// https://www.codingame.com/ide/puzzle/brackets-extreme-edition

const val BALANCED = "true"
const val UN_BALANCED = "false"

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val expression = input.next()

    var bracketBalance = 0

    // Monitor bracket-balance for given expr,
    // if balance < 0 : "{}}" - missing '{'
    // if balance != 0 : "{" - missing '}'
    // than the expr not balanced.
    expression.forEach {
        bracketBalance += when (it) {
            '(' -> 1
            ')' -> -1
            '[' -> 2
            ']' -> -2
            '{' -> 3
            '}' -> -3
            else -> 0
        }
        if (bracketBalance < 0) {
            println(UN_BALANCED)
            return
        }
    }

    val answer = if (bracketBalance == 0) BALANCED else UN_BALANCED
    println(answer)
}