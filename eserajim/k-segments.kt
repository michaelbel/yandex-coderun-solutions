import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun canCover(points: LongArray, k: Int, length: Long): Boolean {
    var segmentsUsed = 1
    var segmentStart = points[0]
    for (i in 1 until points.size) {
        if (points[i] - segmentStart > length) {
            segmentsUsed++
            segmentStart = points[i]
        }
    }
    return segmentsUsed <= k
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, k) = reader.readLine().split(" ").map { it.toInt() }
    val points = reader.readLine().split(" ").map { it.toLong() }.sorted().toLongArray()
    var left = 0L
    var right = points.last() - points.first()
    var answer = right
    while (left <= right) {
        val mid = left + (right - left) / 2
        if (canCover(points, k, mid)) {
            answer = mid
            right = mid - 1
        } else {
            left = mid + 1
        }
    }
    writer.write(answer.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
