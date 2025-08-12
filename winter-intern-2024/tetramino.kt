import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val board = Array(8) { reader.readLine().toCharArray() }

    val tetrominoes = listOf(
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 1)),
        listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(2, 1)),
        listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 0))
    )

    var count = 0
    for (shape in tetrominoes) {
        var maxRow = 0
        var maxCol = 0
        for ((r, c) in shape) {
            if (r > maxRow) maxRow = r
            if (c > maxCol) maxCol = c
        }
        val height = maxRow + 1
        val width = maxCol + 1

        for (i in 0..(8 - height)) {
            for (j in 0..(8 - width)) {
                var valid = true
                for ((dr, dc) in shape) {
                    if (board[i + dr][j + dc] != '.') {
                        valid = false
                        break
                    }
                }
                if (valid) count++
            }
        }
    }

    writer.write(count.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
