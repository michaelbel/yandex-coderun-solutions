import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val matrix = Array(n) { IntArray(n) }
    for (i in 0 until n) {
        val row = reader.readLine().split(" ").map { it.toInt() }
        for (j in 0 until n) {
            matrix[i][j] = row[j]
        }
    }
    val usedNumbers = mutableSetOf<Int>()
    for (i in 0 until n) {
        for (j in 0 until n) {
            if (matrix[i][j] != 0) {
                usedNumbers.add(matrix[i][j])
            }
        }
    }
    val remainingNumbers = mutableListOf<Int>()
    for (num in 1..n * n) {
        if (num !in usedNumbers) {
            remainingNumbers.add(num)
        }
    }
    var index = 0
    val result = Array(n) { IntArray(n) }
    for (i in 0 until n) {
        for (j in 0 until n) {
            if (matrix[i][j] != 0) {
                result[i][j] = matrix[i][j]
            } else {
                result[i][j] = remainingNumbers[index]
                index++
            }
        }
    }
    for (i in 0 until n) {
        writer.write(result[i].joinToString(" "))
        writer.newLine()
    }
    reader.close()
    writer.close()
}
