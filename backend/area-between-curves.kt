import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val line = reader.readLine().split(" ")
    val n = line[0].toInt()
    val m = line[1].toInt()
    val fBoundaries = reader.readLine().split(" ").map { it.toDouble() }.toDoubleArray()
    val fA = DoubleArray(n)
    val fB = DoubleArray(n)
    val fC = DoubleArray(n)
    for (i in 0 until n) {
        val parts = reader.readLine().split(" ")
        fA[i] = parts[0].toDouble()
        fB[i] = parts[1].toDouble()
        fC[i] = parts[2].toDouble()
    }
    val gBoundaries = reader.readLine().split(" ").map { it.toDouble() }.toDoubleArray()
    val gA = DoubleArray(m)
    val gB = DoubleArray(m)
    val gC = DoubleArray(m)
    for (i in 0 until m) {
        val parts = reader.readLine().split(" ")
        gA[i] = parts[0].toDouble()
        gB[i] = parts[1].toDouble()
        gC[i] = parts[2].toDouble()
    }
    var totalArea = 0.0
    var i = 0
    var j = 0
    var currentX = fBoundaries[0]
    val Lend = fBoundaries[n]
    val eps = 1e-12
    while (currentX < Lend - eps && i < n && j < m) {
        val nextX = min(fBoundaries[i + 1], gBoundaries[j + 1])
        val A = fA[i] - gA[j]
        val B = fB[i] - gB[j]
        val C = fC[i] - gC[j]
        totalArea += integrateAbsQuadratic(A, B, C, currentX, nextX)
        if (abs(fBoundaries[i + 1] - nextX) < eps) i++
        if (abs(gBoundaries[j + 1] - nextX) < eps) j++
        currentX = nextX
    }
    writer.write(String.format("%.10f", totalArea))
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}

fun integrateAbsQuadratic(A: Double, B: Double, C: Double, L: Double, R: Double): Double {
    val eps = 1e-12
    if (R - L < eps) return 0.0
    if (abs(A) < eps && abs(B) < eps) return abs(C) * (R - L)
    fun F(x: Double): Double = A * x * x * x / 3.0 + B * x * x / 2.0 + C * x
    fun p(x: Double): Double = A * x * x + B * x + C
    val pts = DoubleArray(4)
    var cnt = 0
    pts[cnt++] = L
    if (abs(A) < eps) {
        if (abs(B) >= eps) {
            val root = -C / B
            if (root > L + eps && root < R - eps) pts[cnt++] = root
        }
    } else {
        val D = B * B - 4.0 * A * C
        if (D >= 0.0) {
            val sqrtD = sqrt(D)
            val r1 = (-B - sqrtD) / (2.0 * A)
            val r2 = (-B + sqrtD) / (2.0 * A)
            if (r1 > L + eps && r1 < R - eps) pts[cnt++] = r1
            if (r2 > L + eps && r2 < R - eps) pts[cnt++] = r2
        }
    }
    pts[cnt++] = R
    for (a in 0 until cnt) {
        for (b in a + 1 until cnt) {
            if (pts[a] > pts[b]) {
                val t = pts[a]
                pts[a] = pts[b]
                pts[b] = t
            }
        }
    }
    var area = 0.0
    for (t in 0 until cnt - 1) {
        val a = pts[t]
        val b = pts[t + 1]
        if (b - a < eps) continue
        val mid = (a + b) / 2.0
        val sign = if (p(mid) >= 0) 1.0 else -1.0
        area += sign * (F(b) - F(a))
    }
    return abs(area)
}
