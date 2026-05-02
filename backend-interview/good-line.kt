import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val N = reader.readLine().toInt()
    val counts = LongArray(N) { reader.readLine().toLong() }

    var goodness = 0L

    // Проходим по всем парам соседних букв (a-b, b-c, ...)
    for (i in 0 until N - 1) {
        goodness += minOf(counts[i], counts[i + 1])
    }

    writer.write(goodness.toString())
    writer.newLine()

    reader.close()
    writer.close()
}
