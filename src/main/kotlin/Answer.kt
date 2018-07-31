import java.util.*

/**
 * orDer oF succeSsion puzzle from CodInGame.com
 * Difficulty : Easy
 * https://www.codingame.com/ide/puzzle/order-of-succession
 */

enum class Gender(val gender: String) {
    M("M"), F("F")
}


data class Person(
    val name: String = "empty",
    val parent: String = "empty",
    val birth: Long = 0,
    val death: Long = 0,
    val isAnglican: Boolean = true,
    val gender: Gender = Gender.F
)

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt()
    val succession = Array(n) { Person() }

    for (i in 0 until n) {
        val person = Person(input.next(), input.next(), input.nextLong(), run {
            val temp = input.next()
            if (temp == "-") 0 else temp.toLong()
        }, run { input.next() == "Anglican" }, Gender.valueOf(input.next()))
        succession[i] = person
    }

    //        18
    //        Elizabeth - 1926 - Anglican F

    //                Charles Elizabeth 1948 - Anglican M

    //                William Charles 1982 - Anglican M
    //                George William 2013 - Anglican M
    //                Charlotte William 2015 - Anglican F

    //                Henry Charles 1984 - Anglican M

    //                Andrew Elizabeth 1960 - Anglican M
    //                Beatrice Andrew 1988 - Anglican F
    //                Eugenie Andrew 1990 - Anglican F

    //                Edward Elizabeth 1964 - Anglican M
    //                James Edward 2007 - Anglican M
    //                Louise Edward 2003 - Anglican F

    //                Anne Elizabeth 1950 - Anglican F
    //                Peter Anne 1977 - Anglican M

    //                Savannah Peter 2010 - Anglican F
    //                Isla Peter 2012 - Anglican F

    //                Zara Anne 1981 - Anglican F
    //                Mia Zara 2014 - Anglican F

    succession.forEach { println(it) }
}