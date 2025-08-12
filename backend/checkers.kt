import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    val w = reader.readLine().toInt()
    val white = Array(w) { reader.readLine().split(" ").map { it.toInt() }.let { it[0] to it[1] } }
    val b = reader.readLine().toInt()
    val black = Array(b) { reader.readLine().split(" ").map { it.toInt() }.let { it[0] to it[1] } }
    val turn = reader.readLine()
    val board = Array(n + 1) { IntArray(m + 1) }
    for ((i, j) in white) board[i][j] = 1
    for ((i, j) in black) board[i][j] = 2
    val directions = listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
    val player = if (turn == "white") 1 else 2
    val opponent = if (player == 1) 2 else 1
    val positions = if (player == 1) white else black
    var canCapture = false
    for ((i, j) in positions) {
        for ((di, dj) in directions) {
            val ni = i + di
            val nj = j + dj
            val jumpI = i + 2 * di
            val jumpJ = j + 2 * dj
            if (ni in 1..n && nj in 1..m && jumpI in 1..n && jumpJ in 1..m) {
                if (board[ni][nj] == opponent && board[jumpI][jumpJ] == 0) {
                    canCapture = true
                    break
                }
            }
        }
        if (canCapture) break
    }
    writer.write(if (canCapture) "Yes" else "No")
    writer.newLine()
    reader.close()
    writer.close()
}
