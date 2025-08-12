import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.sqrt
import kotlin.math.max
import kotlin.math.min
import kotlin.math.PI
import kotlin.math.abs

fun simpson(f: (Double) -> Double, a: Double, b: Double): Double {
    val c = (a + b) / 2.0
    return (b - a) / 6.0 * (f(a) + 4.0 * f(c) + f(b))
}

fun adaptiveSimpson(f: (Double) -> Double, a: Double, b: Double, eps: Double, whole: Double = simpson(f, a, b)): Double {
    val c = (a + b) / 2.0
    val left = simpson(f, a, c)
    val right = simpson(f, c, b)
    if (abs(left + right - whole) <= 15 * eps) {
        return left + right + (left + right - whole) / 15.0
    }
    return adaptiveSimpson(f, a, c, eps / 2.0, left) + adaptiveSimpson(f, c, b, eps / 2.0, right)
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val R = firstLine[1].toDouble()
    var totalExpectation = 0.0
    for (i in 1..n) {
        val line = reader.readLine().split(" ")
        val x = line[0].toDouble()
        val y = line[1].toDouble()
        var area = 0.0
        if (x - R >= 0.0 && x + R <= 1.0 && y - R >= 0.0 && y + R <= 1.0) {
            area = PI * R * R
        } else if (((x - 0.0) * (x - 0.0) + (y - 0.0) * (y - 0.0) <= R * R) &&
            ((x - 0.0) * (x - 0.0) + (y - 1.0) * (y - 1.0) <= R * R) &&
            ((x - 1.0) * (x - 1.0) + (y - 0.0) * (y - 0.0) <= R * R) &&
            ((x - 1.0) * (x - 1.0) + (y - 1.0) * (y - 1.0) <= R * R)) {
            area = 1.0
        } else {
            val a = max(0.0, x - R)
            val b = min(1.0, x + R)
            val f: (Double) -> Double = { xi ->
                val radicand = R * R - (xi - x) * (xi - x)
                if (radicand < 0) 0.0 else {
                    val dy = sqrt(radicand)
                    val yLow = max(0.0, y - dy)
                    val yHigh = min(1.0, y + dy)
                    max(0.0, yHigh - yLow)
                }
            }
            area = adaptiveSimpson(f, a, b, 1e-10)
        }
        totalExpectation += area
    }
    writer.write(String.format("%.10f", totalExpectation))
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
