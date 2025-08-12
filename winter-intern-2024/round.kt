import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.floor

data class FractionalPartInfo(val fractionalPart: Double, val originalIndex: Int)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val x = firstLine[1].toLong()

    val a = reader.readLine().split(" ").map { it.toLong() }
    val sumA = a.sum()

    val c = LongArray(n)
    val fractionalParts = mutableListOf<FractionalPartInfo>()
    var currentSumC: Long = 0

    for (i in 0 until n) {
        val bi = if (sumA == 0L) 0.0 else (x.toDouble() * a[i].toDouble()) / sumA.toDouble()
        val floorBi = floor(bi).toLong()
        c[i] = floorBi
        currentSumC += floorBi
        val fraction = bi - floorBi
        if (fraction > 1e-9) {
             fractionalParts.add(FractionalPartInfo(fraction, i))
        }
    }

    val diff = x - currentSumC
    fractionalParts.sortByDescending { it.fractionalPart }

    for (k in 0 until diff.toInt()) {
        if (k < fractionalParts.size) {
            val indexToIncrement = fractionalParts[k].originalIndex
            c[indexToIncrement]++
        } else {
            break
        }
    }

    writer.write(c.joinToString(" "))
    writer.newLine()
    writer.flush()
    writer.close()
    reader.close()
}
