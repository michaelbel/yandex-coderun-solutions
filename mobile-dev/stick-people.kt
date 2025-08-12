import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun dissatisfaction(neck: Int, collar: Int): Int {
    return when {
        collar < neck -> 0
        collar - neck <= 100 -> (collar - neck) / 2
        else -> 30
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val neckSizes = reader.readLine().split(" ").map { it.toInt() }
    val collarSizes = reader.readLine().split(" ").map { it.toInt() }
    var minDissatisfaction = Int.MAX_VALUE
    var bestStart = 0
    for (start in 0 until n) {
        var totalDissatisfaction = 0
        for (i in 0 until n) {
            val index = (start + i) % n
            totalDissatisfaction += dissatisfaction(neckSizes[index], collarSizes[i])
        }
        if (totalDissatisfaction < minDissatisfaction) {
            minDissatisfaction = totalDissatisfaction
            bestStart = start
        }
    }
    writer.write("${bestStart + 1} $minDissatisfaction")
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
