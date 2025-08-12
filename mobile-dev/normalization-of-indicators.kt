import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.abs

fun findClosestCanonical(values: IntArray, query: Int): Int {
    var left = 0
    var right = values.size - 1
    var best = values[0]
    while (left <= right) {
        val mid = (left + right) / 2
        if (abs(values[mid] - query) < abs(best - query)) {
            best = values[mid]
        }
        if (values[mid] < query) {
            left = mid + 1
        } else {
            right = mid - 1
        }
    }
    return best
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val canonical = reader.readLine().split(" ").map { it.toInt() }.sorted().toIntArray()
    val m = reader.readLine().toInt()
    repeat(m) {
        val query = reader.readLine().toInt()
        writer.write("${findClosestCanonical(canonical, query)}\n")
    }
    writer.flush()
    reader.close()
    writer.close()
}
