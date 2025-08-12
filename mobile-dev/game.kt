import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m, k) = reader.readLine().split(" ").map { it.toInt() }
    val field = Array(n) { reader.readLine().split(" ").map { it.toInt() }.toIntArray() }
    val changes = Array(n) { IntArray(m) }
    var current = field
    repeat(k) { iter ->
        val next = Array(n) { IntArray(m) }
        for (i in 0 until n) {
            for (j in 0 until m) {
                var stableNeighbors = 0
                var activeNeighbors = 0
                val directions = arrayOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
                for ((di, dj) in directions) {
                    val ni = i + di
                    val nj = j + dj
                    if (ni in 0 until n && nj in 0 until m) {
                        if (current[ni][nj] == 2) stableNeighbors++
                        if (current[ni][nj] == 2 || current[ni][nj] == 3) activeNeighbors++
                    }
                }
                next[i][j] = when {
                    stableNeighbors > 1 -> 2
                    activeNeighbors > 0 -> 3
                    else -> 1
                }
                if (iter == 0 && next[i][j] != current[i][j]) changes[i][j]++
                else if (iter > 0 && next[i][j] != current[i][j]) changes[i][j]++
            }
        }
        current = next
    }
    for (i in 0 until n) {
        writer.write(changes[i].joinToString(" "))
        writer.newLine()
    }
    reader.close()
    writer.close()
}
