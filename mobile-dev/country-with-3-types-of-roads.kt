import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun multiply(A: Array<Array<Long>>, B: Array<Array<Long>>, n: Int): Array<Array<Long>> {
    val C = Array(n + 1) { Array(n + 1) { 0L } }
    for (i in 1..n) {
        for (j in 1..n) {
            var sum = 0L
            for (k in 1..n) {
                if (A[i][k] != 0L) {
                    sum += A[i][k] * B[k][j]
                }
            }
            C[i][j] = sum
        }
    }
    return C
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val m = firstLine[1].toInt()
    val matrixG = Array(n + 1) { Array(n + 1) { 0L } }
    val matrixY = Array(n + 1) { Array(n + 1) { 0L } }
    val matrixR = Array(n + 1) { Array(n + 1) { 0L } }
    for (i in 0 until m) {
        val roadLine = reader.readLine().split(" ")
        val u = roadLine[0].toInt()
        val v = roadLine[1].toInt()
        val color = roadLine[2][0]
        when (color) {
            'g' -> matrixG[u][v]++
            'y' -> matrixY[u][v]++
            'r' -> matrixR[u][v]++
        }
    }
    val matrixGY = multiply(matrixG, matrixY, n)
    val matrixGYR = multiply(matrixGY, matrixR, n)
    val q = reader.readLine().toInt()
    for (i in 0 until q) {
        val queryLine = reader.readLine().split(" ")
        val a = queryLine[0].toInt()
        val b = queryLine[1].toInt()
        val result = matrixGYR[a][b]
        writer.write(result.toString())
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
