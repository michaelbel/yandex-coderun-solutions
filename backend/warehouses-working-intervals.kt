import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class Interval(val warehouse: Int, val start: String, val end: String, val type: String)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val lines = mutableListOf<String>()
    var line = reader.readLine()
    while (line != null) {
        lines.add(line)
        line = reader.readLine()
    }
    val intervals = mutableListOf<Interval>()
    for (l in lines) {
        val parts = l.split(",")
        val warehouse = parts[0].toInt()
        val (start, end) = parts[1].split(" ")
        val type = parts[2]
        if (type == "NULL") {
            intervals.add(Interval(warehouse, start, end, "KGT"))
            intervals.add(Interval(warehouse, start, end, "COLD"))
            intervals.add(Interval(warehouse, start, end, "OTHER"))
        } else {
            intervals.add(Interval(warehouse, start, end, type))
        }
    }
    val grouped = intervals.groupBy { it.warehouse to it.type }
    val result = mutableListOf<Interval>()
    for ((key, group) in grouped) {
        val (warehouse, type) = key
        val sorted = group.sortedBy { it.start }
        val merged = mutableListOf<Interval>()
        var current = sorted[0]
        for (i in 1 until sorted.size) {
            val next = sorted[i]
            if (current.end >= next.start) {
                current = Interval(warehouse, current.start, maxOf(current.end, next.end), type)
            } else {
                merged.add(current)
                current = next
            }
        }
        merged.add(current)
        result.addAll(merged)
    }
    val sortedResult = result.sortedWith(compareBy(
        { it.warehouse },
        { when (it.type) { "KGT" -> 1; "COLD" -> 2; "OTHER" -> 3; else -> 4 } },
        { it.start }
    ))
    for (interval in sortedResult) {
        writer.write("${interval.warehouse},${interval.start} ${interval.end},${interval.type}")
        writer.newLine()
    }
    reader.close()
    writer.close()
}
