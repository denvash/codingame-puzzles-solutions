package easy.timeFlies

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

// https://www.codingame.com/ide/puzzle/how-time-flies

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
    val date1 = LocalDate.parse(input.next(), formatter)
    val date2 = LocalDate.parse(input.next(), formatter)

    val years = ChronoUnit.YEARS.between(date1, date2)
    val months = ChronoUnit.MONTHS.between(date1, date2) - years * 12
    val days = ChronoUnit.DAYS.between(date1, date2)

    val yearsStr = "year${if (years != 1.toLong()) "s" else ""}"

    val monthStr = "month${if (months != 1.toLong()) "s" else ""}"

    println(
        """
        ${if (years != 0.toLong()) "$years $yearsStr, " else ""}${if (months != 0.toLong()) "$months $monthStr, " else ""}total $days days
    """.trimIndent()
    )
}