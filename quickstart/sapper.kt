import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (n, m, k) = reader.readLine().split(" ").map(String::toInt)
    val field = Array(n) { IntArray(m) { 0 } }

    repeat(k) {
        val (p, q) = reader.readLine().split(" ").map(String::toInt)
        val x = p - 1
        val y = q - 1
        field[x][y] = -1
        for (i in -1..1) {
            for (j in -1..1) {
                val ni = x + i
                val nj = y + j
                if (ni in 0 until n && nj in 0 until m && field[ni][nj] != -1) {
                    field[ni][nj]++
                }
            }
        }
    }

    for (i in 0 until n) {
        writer.write(field[i].joinToString(" ") { if (it == -1) "*" else it.toString() })
        writer.newLine()
    }

    reader.close()
    writer.flush()
    writer.close()
}
