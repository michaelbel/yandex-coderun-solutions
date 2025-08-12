import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val inputLine = reader.readLine()
    val parts = inputLine.split(" ")
    val nDays = parts[0].toInt()
    val weekday = parts[1]
    val offset = when (weekday) {
        "Monday" -> 0
        "Tuesday" -> 1
        "Wednesday" -> 2
        "Thursday" -> 3
        "Friday" -> 4
        "Saturday" -> 5
        else -> 6
    }
    val calendarElements = mutableListOf<String>()
    repeat(offset) { calendarElements.add("..") }
    for (day in 1..nDays) {
        val formatted = if (day < 10) ".$day" else day.toString()
        calendarElements.add(formatted)
    }
    for (i in calendarElements.indices) {
        writer.write(calendarElements[i])
        if ((i + 1) % 7 == 0) {
            if (i < calendarElements.size - 1) writer.newLine()
        } else {
            writer.write(" ")
        }
    }
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
