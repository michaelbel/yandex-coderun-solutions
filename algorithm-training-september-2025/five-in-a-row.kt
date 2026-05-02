import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine()
    if (firstLine == null || firstLine.isEmpty()) {
        writer.write("No")
        writer.newLine()
        writer.flush()
        return
    }

    val st = StringTokenizer(firstLine)
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()

    val grid = Array(n) { CharArray(m) }
    for (i in 0 until n) {
        var line = reader.readLine()
        // На всякий случай дочитаем, если строка короче m
        while (line != null && line.length < m) {
            val extra = reader.readLine() ?: break
            line += extra
        }
        if (line == null) {
            // некорректный ввод, но просто выходим
            writer.write("No")
            writer.newLine()
            writer.flush()
            return
        }
        for (j in 0 until m) {
            grid[i][j] = line[j]
        }
    }

    val dx = intArrayOf(0, 1, 1, 1)   // вправо, вниз, диагональ вниз-вправо, диагональ вниз-влево
    val dy = intArrayOf(1, 0, 1, -1)

    fun inBounds(x: Int, y: Int): Boolean = x in 0 until n && y in 0 until m

    for (i in 0 until n) {
        for (j in 0 until m) {
            val c = grid[i][j]
            if (c == '.') continue

            for (dir in 0 until 4) {
                val xEnd = i + dx[dir] * 4
                val yEnd = j + dy[dir] * 4
                if (!inBounds(xEnd, yEnd)) continue

                var ok = true
                var step = 1
                while (step <= 4) {
                    val x = i + dx[dir] * step
                    val y = j + dy[dir] * step
                    if (grid[x][y] != c) {
                        ok = false
                        break
                    }
                    step++
                }
                if (ok) {
                    writer.write("Yes")
                    writer.newLine()
                    writer.flush()
                    return
                }
            }
        }
    }

    writer.write("No")
    writer.newLine()
    writer.flush()
}
