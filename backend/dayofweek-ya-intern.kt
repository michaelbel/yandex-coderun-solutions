import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun isLeapYear(year: Int): Boolean {
    return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val daysInMonth = intArrayOf(0,31,28,31,30,31,30,31,31,30,31,30,31)
    val months = mapOf(
        "January" to 1,"February" to 2,"March" to 3,"April" to 4,
        "May" to 5,"June" to 6,"July" to 7,"August" to 8,
        "September" to 9,"October" to 10,"November" to 11,"December" to 12
    )
    val daysOfWeek = listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
    val baseYear = 1980
    val baseDayOfWeek = 1
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        val parts = line!!.split(" ")
        val day = parts[0].toInt()
        val monthName = parts[1]
        val year = parts[2].toInt()
        val month = months[monthName]!!
        var totalDays = 0
        for (y in baseYear until year) {
            totalDays += if (isLeapYear(y)) 366 else 365
        }
        val leap = isLeapYear(year)
        for (m in 1 until month) {
            totalDays += if (m == 2 && leap) 29 else daysInMonth[m]
        }
        totalDays += day - 1
        val dayIndex = (totalDays + baseDayOfWeek) % 7
        writer.write(daysOfWeek[dayIndex])
        writer.newLine()
    }
    reader.close()
    writer.close()
}
