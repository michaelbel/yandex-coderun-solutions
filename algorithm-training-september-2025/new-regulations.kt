import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.abs

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val first = StringTokenizer(reader.readLine())
    val x = first.nextToken().toLong()
    val y = first.nextToken().toLong()

    val second = StringTokenizer(reader.readLine())
    val f = second.nextToken().toLong()
    val g = second.nextToken().toLong()

    val dx = abs(f - x)
    val dy = abs(g - y)

    val result: Long = if (dx == 0L && dy == 0L) {
        0L
    } else {
        val s = dx + dy
        if (dx == 0L || dy == 0L) {
            3L * s - 3L
        } else {
            3L * s - 5L
        }
    }

    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
