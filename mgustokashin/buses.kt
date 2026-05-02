import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Comparator

data class Event(val time: Int, val city: Int, val type: Int)

fun parseTime(timeStr: String): Int {
    val parts = timeStr.split(":")
    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    return hours * 60 + minutes
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val m = firstLine[1].toInt()

    val dailyBalance = IntArray(n + 1) { 0 }
    var midnightCrossings = 0L
    val events = mutableListOf<Event>()

    for (i in 0 until m) {
        val line = reader.readLine().split(" ")
        val f = line[0].toInt()
        val xStr = line[1]
        val g = line[2].toInt()
        val yStr = line[3]

        val xMinutes = parseTime(xStr)
        val yMinutes = parseTime(yStr)

        dailyBalance[f]--
        dailyBalance[g]++

        if (yMinutes < xMinutes) {
            midnightCrossings++
        }

        events.add(Event(xMinutes, f, -1))
        events.add(Event(yMinutes, g, +1))
    }

    for (city in 1..n) {
        if (dailyBalance[city] != 0) {
            writer.write("-1\n")
            writer.flush()
            return
        }
    }

    val eventComparator = Comparator<Event> { e1, e2 ->
        if (e1.time != e2.time) {
            e1.time.compareTo(e2.time)
        } else {
            e2.type.compareTo(e1.type)
        }
    }
    events.sortWith(eventComparator)

    val currentBuses = IntArray(n + 1) { 0 }
    val minNeeded = IntArray(n + 1) { 0 }
    var totalInitialNeeded = 0L

    for (event in events) {
        val city = event.city
        val type = event.type

        if (type == 1) {
            currentBuses[city]++
        } else {
            currentBuses[city]--
            if (currentBuses[city] < 0) {
                minNeeded[city]++
                currentBuses[city] = 0
            }
        }
    }

    for (neededCount in minNeeded) {
        totalInitialNeeded += neededCount.toLong()
    }

    val result = totalInitialNeeded + midnightCrossings
    writer.write("$result\n")
    writer.flush()
}
