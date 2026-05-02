import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max

private data class Berry(val id: Int, val a: Long, val b: Long) {
    val d: Long = a - b
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val berries = mutableListOf<Berry>()
    for (i in 1..n) {
        val parts = reader.readLine().split(" ")
        val a = parts[0].toLong()
        val b = parts[1].toLong()
        berries.add(Berry(id = i, a = a, b = b))
    }

    val posGroup = berries.filter { it.d >= 0 }
    val negGroup = berries.filter { it.d < 0 }

    val sortedPosGroup = posGroup.sortedBy { it.b }
    val sortedNegGroup = negGroup.sortedByDescending { it.a }

    val optimalOrder = sortedPosGroup + sortedNegGroup

    var currentHeight = 0L
    var maxPeakHeight = 0L

    for (berry in optimalOrder) {
        val peakToday = currentHeight + berry.a
        maxPeakHeight = max(maxPeakHeight, peakToday)
        currentHeight += berry.d
    }

    writer.write(maxPeakHeight.toString())
    writer.newLine()
    writer.write(optimalOrder.map { it.id }.joinToString(" "))
    writer.newLine()

    reader.close()
    writer.close()
}
