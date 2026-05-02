import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(" ").map(String::toInt)
    val start = IntArray(n)
    val end = IntArray(n)
    val points = Array(m) { Pair(0, 0) }

    repeat(n) { i ->
        val (a, b) = reader.readLine().split(" ").map(String::toInt)
        start[i] = minOf(a, b)
        end[i] = maxOf(a, b)
    }

    val pointValues = reader.readLine().split(" ").mapIndexed { index, x -> Pair(x.toInt(), index) }

    start.sort()
    end.sort()
    val sortedPoints = pointValues.sortedBy { it.first }

    val result = IntArray(m)
    var activeSegments = 0
    var i = 0
    var j = 0

    for ((x, index) in sortedPoints) {
        while (i < n && start[i] <= x) {
            activeSegments++
            i++
        }
        while (j < n && end[j] < x) {
            activeSegments--
            j++
        }
        result[index] = activeSegments
    }

    writer.write(result.joinToString(" "))
    writer.newLine()
    writer.flush()
    writer.close()
}
