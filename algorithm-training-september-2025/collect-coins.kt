import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val nLine = reader.readLine() ?: run {
        writer.write("0")
        writer.newLine()
        writer.flush()
        return
    }
    val n = nLine.trim().toInt()

    val grid = Array(n) { CharArray(3) }
    for (i in 0 until n) {
        var line = reader.readLine()
        while (line != null && line.length < 3) {
            val extra = reader.readLine() ?: break
            line += extra
        }
        if (line == null) {
            // недополненный ввод, но задачи так не делают
            break
        }
        for (j in 0 until 3) {
            grid[i][j] = line[j]
        }
    }

    val NEG = -1_000_000_000
    var dpPrev = IntArray(3) { NEG }
    var dpCur = IntArray(3) { NEG }

    // старт из первой строки: можно в любую не-стену
    var allWallsFirstRow = true
    for (c in 0 until 3) {
        if (grid[0][c] != 'W') {
            allWallsFirstRow = false
            dpPrev[c] = if (grid[0][c] == 'C') 1 else 0
        }
    }

    if (allWallsFirstRow) {
        writer.write("0")
        writer.newLine()
        writer.flush()
        return
    }

    var answer = 0
    for (c in 0 until 3) {
        if (dpPrev[c] > answer) answer = dpPrev[c]
    }

    // переходы вниз
    for (r in 1 until n) {
        for (c in 0 until 3) {
            dpCur[c] = NEG
        }

        for (c in 0 until 3) {
            if (grid[r][c] == 'W') continue
            var bestFrom = NEG
            for (dc in -1..1) {
                val pc = c + dc
                if (pc in 0..2) {
                    if (dpPrev[pc] > bestFrom) bestFrom = dpPrev[pc]
                }
            }
            if (bestFrom == NEG) continue
            val add = if (grid[r][c] == 'C') 1 else 0
            dpCur[c] = bestFrom + add
            if (dpCur[c] > answer) answer = dpCur[c]
        }

        val tmp = dpPrev
        dpPrev = dpCur
        dpCur = tmp
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
