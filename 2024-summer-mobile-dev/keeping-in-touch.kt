import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import kotlin.math.abs

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))
    val n = reader.readLine().toInt()
    val x = IntArray(n)
    val y = IntArray(n)
    val r = IntArray(n)
    repeat(n) { i ->
        val parts = reader.readLine().split(' ')
        x[i] = parts[0].toInt()
        y[i] = parts[1].toInt()
        r[i] = parts[2].toInt()
    }
    val startParts = reader.readLine().split(' ')
    val xs = startParts[0].toInt()
    val ys = startParts[1].toInt()
    val endParts = reader.readLine().split(' ')
    val xf = endParts[0].toInt()
    val yf = endParts[1].toInt()
    val coversStart = BooleanArray(n)
    val coversEnd = BooleanArray(n)
    for (i in 0 until n) {
        if (abs(x[i] - xs) <= r[i] && abs(y[i] - ys) <= r[i]) {
            coversStart[i] = true
        }
        if (abs(x[i] - xf) <= r[i] && abs(y[i] - yf) <= r[i]) {
            coversEnd[i] = true
        }
    }
    val visited = BooleanArray(n)
    val queue = ArrayDeque<Int>()
    for (i in 0 until n) {
        if (coversStart[i]) {
            visited[i] = true
            queue.addLast(i)
        }
    }
    var reachable = false
    while (queue.isNotEmpty()) {
        val i = queue.removeFirst()
        if (coversEnd[i]) {
            reachable = true
            break
        }
        for (j in 0 until n) {
            if (!visited[j]
                && abs(x[i] - x[j]) <= r[i] + r[j] + 1
                && abs(y[i] - y[j]) <= r[i] + r[j] + 1
            ) {
                visited[j] = true
                queue.addLast(j)
            }
        }
    }
    writer.write(if (reachable) "1" else "0")
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
