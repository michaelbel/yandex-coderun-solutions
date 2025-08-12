import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val schedule = mutableMapOf<Int, MutableMap<String, MutableList<Triple<Int, Int, List<String>>>>>()

private fun timeToMinutes(time: String): Int {
    val (hh, mm) = time.split(":").map { it.toInt() }
    return hh * 60 + mm
}

private fun isOverlapping(start1: Int, duration1: Int, start2: Int, duration2: Int): Boolean {
    return !(start1 + duration1 <= start2 || start2 + duration2 <= start1)
}

private fun addAppointment(day: Int, time: String, duration: Int, names: List<String>, writer: BufferedWriter) {
    val startTime = timeToMinutes(time)
    val conflicts = mutableSetOf<String>()
    for (name in names) {
        val meetings = schedule.getOrPut(day) { mutableMapOf() }.getOrPut(name) { mutableListOf() }
        if (meetings.any { isOverlapping(it.first, it.second, startTime, duration) }) {
            conflicts.add(name)
        }
    }
    if (conflicts.isEmpty()) {
        for (name in names) {
            schedule[day]!![name]!!.add(Triple(startTime, duration, names))
        }
        writer.write("OK\n")
    } else {
        writer.write("FAIL\n")
        writer.write(conflicts.joinToString(" ") + "\n")
    }
}

private fun printSchedule(day: Int, name: String, writer: BufferedWriter) {
    schedule[day]?.get(name)?.sortedBy { it.first }?.forEach { (start, duration, names) ->
        val hh = start / 60
        val mm = start % 60
        writer.write("%02d:%02d %d %s\n".format(hh, mm, duration, names.joinToString(" ")))
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    repeat(n) {
        val parts = reader.readLine().split(" ")
        when (parts[0]) {
            "APPOINT" -> {
                val day = parts[1].toInt()
                val time = parts[2]
                val duration = parts[3].toInt()
                val names = parts.drop(5)
                addAppointment(day, time, duration, names, writer)
            }
            "PRINT" -> {
                val day = parts[1].toInt()
                val name = parts[2]
                printSchedule(day, name, writer)
            }
        }
    }
    writer.flush()
    reader.close()
    writer.close()
}
