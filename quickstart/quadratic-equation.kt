import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import kotlin.math.sqrt

fun solveQuadratic(a: Double, b: Double, c: Double): Pair<Int, List<Double>> {
    val d = b * b - 4 * a * c
    if (d < 0) return 0 to emptyList()
    val sqrtD = sqrt(d)
    val x1 = (-b + sqrtD) / (2 * a)
    val x2 = (-b - sqrtD) / (2 * a)
    if (d == 0.0) return 1 to listOf(x1)
    return 2 to listOf(minOf(x1, x2), maxOf(x1, x2))
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val input = reader.readLine()
    if (input.isNullOrBlank()) {
        writer.write("0")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }

    val (a, b, c) = input.trim().split(" ").map { it.toDouble() }
    
    val (count, roots) = solveQuadratic(a, b, c)
    
    writer.write("$count")
    writer.newLine()
    
    if (count > 0) {
        writer.write(roots.joinToString(" ") { String.format("%.6f", it) })
    }
    writer.newLine()
    
    reader.close()
    writer.close()
}
