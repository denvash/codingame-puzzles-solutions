package easy.orderOfSuccession

import java.util.*

// https://www.codingame.com/ide/puzzle/order-of-succession

enum class Gender(val gender: String) {
    M("M"), F("F")
}

data class Person(
    val name: String,
    val parent: String,
    val birth: Long,
    val death: Long,
    val isAnglican: Boolean,
    val gender: Gender
)

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt()
    val persons = LinkedList<Person>()

    // Each person insert to List
    for (i in 0 until n) {
        val person = Person(
            input.next(),
            input.next(),
            input.nextLong(),
            run {
                val temp = input.next()
                if (temp == "-") 0 else temp.toLong()
            },
            run { input.next() == "Anglican" },
            Gender.valueOf(input.next())
        )
        persons.addLast(person)
    }

    // Ordering rules (a) in order of generation (b) in order of gender (c) in order of age (year of birth)
    // First sort - persons sorted by parents
    val succession = persons.sortedWith(compareBy({ it.parent }, { it.gender }, { it.birth })).toMutableList()

    // Sub sort - persons sorted by successors
    for (i in succession.indices) {
        val parent = succession[i].name
        for (k in i + 1..succession.size) {
            val targetIndex = succession.indexOfFirst {
                it.parent == parent && k <= succession.indexOf(it)
            }
            succession.pushBefore(k, targetIndex)
        }
    }
    // (a) exclude dead people (b) exclude people who are catholic (but include siblings of catholic people)
    succession.filter { it.death == 0.toLong() && it.isAnglican }.forEach { println(it.name) }
}

private fun <E> MutableList<E>.pushBefore(pushBefore: Int, targetIndex: Int) {
    for (i in pushBefore..targetIndex) {
        this.swap(i, targetIndex)
    }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 == -1 || index2 == -1) return
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}
