import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun multiplyMatrices(a: Array<IntArray>, b: Array<IntArray>, n: Int, m: Int, k: Int): Array<IntArray> {
    val result = Array(n) { IntArray(k) }
    for (i in 0 until n) {
        for (j in 0 until k) {
            var sum = 0
            for (p in 0 until m) {
                sum += a[i][p] * b[p][j]
            }
            result[i][j] = sum
        }
    }
    return result
}

fun transposeMatrix(matrix: Array<IntArray>, rows: Int, cols: Int): Array<IntArray> {
    val transposed = Array(cols) { IntArray(rows) }
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            transposed[j][i] = matrix[i][j]
        }
    }
    return transposed
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val sizeInput = reader.readLine()
    if (sizeInput.isNullOrBlank()) {
        writer.close()
        reader.close()
        return
    }
    val (n, m, k) = sizeInput.trim().split(" ").map { it.toInt() }
    
    val a = Array(n) { 
        val line = reader.readLine()
        if (line.isNullOrBlank()) IntArray(m) { 0 } else line.trim().split(" ").map { it.toInt() }.toIntArray()
    }
    
    val b = Array(m) { 
        val line = reader.readLine()
        if (line.isNullOrBlank()) IntArray(k) { 0 } else line.trim().split(" ").map { it.toInt() }.toIntArray()
    }
    
    val product = multiplyMatrices(a, b, n, m, k)
    
    val result = transposeMatrix(product, n, k)
    
    for (row in result) {
        writer.write(row.joinToString(" "))
        writer.newLine()
    }
    
    reader.close()
    writer.close()
}
