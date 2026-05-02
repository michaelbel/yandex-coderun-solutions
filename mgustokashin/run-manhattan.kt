import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max
import kotlin.math.min
import kotlin.math.ceil
import kotlin.math.floor

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ").map { it.toInt() }
    val t = firstLine[0]
    val d = firstLine[1]
    val n = firstLine[2]

    val maxDistFromOrigin = t * n

    var uMin: Long = -maxDistFromOrigin.toLong()
    var uMax: Long = maxDistFromOrigin.toLong()
    var vMin: Long = -maxDistFromOrigin.toLong()
    var vMax: Long = maxDistFromOrigin.toLong()

    for (i in 1..n) {
        val reading = reader.readLine().split(" ").map { it.toInt() }
        val xi = reading[0].toLong()
        val yi = reading[1].toLong()

        val timeToEnd = t * (n - i)
        val maxDistFromReadingI = (timeToEnd + d).toLong()

        val ui = xi + yi
        val vi = xi - yi

        uMin = max(uMin, ui - maxDistFromReadingI)
        uMax = min(uMax, ui + maxDistFromReadingI)
        vMin = max(vMin, vi - maxDistFromReadingI)
        vMax = min(vMax, vi + maxDistFromReadingI)
    }

    val possiblePoints = ArrayList<Pair<Int, Int>>()

    val xStart = ceil((uMin + vMin).toDouble() / 2.0).toInt()
    val xEnd = floor((uMax + vMax).toDouble() / 2.0).toInt()

    for (x in xStart..xEnd) {
        val yStart = ceil(max(uMin.toDouble() - x, x.toDouble() - vMax)).toInt()
        val yEnd = floor(min(uMax.toDouble() - x, x.toDouble() - vMin)).toInt()

        for (y in yStart..yEnd) {
            possiblePoints.add(Pair(x, y))
        }
    }

    writer.write("${possiblePoints.size}")
    writer.newLine()
    for (point in possiblePoints) {
        writer.write("${point.first} ${point.second}")
        writer.newLine()
    }

    writer.flush()
    writer.close()
    reader.close()
}
