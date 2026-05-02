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
        writer.flush()
        return
    }

    val st = StringTokenizer(firstLine)
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()

    val rowBase = IntArray(n)
    val colBase = IntArray(m)
    val rowQ = IntArray(n)
    val colQ = IntArray(m)
    val isQ = Array(n) { BooleanArray(m) }

    for (i in 0 until n) {
        var line = reader.readLine()
        while (line != null && line.length < m) {
            val next = reader.readLine() ?: break
            line = next
        }
        if (line == null) {
            break
        }

        for (j in 0 until m) {
            when (line[j]) {
                '+' -> {
                    rowBase[i]++
                    colBase[j]++
                }
                '-' -> {
                    rowBase[i]--
                    colBase[j]--
                }
                '?' -> {
                    rowQ[i]++
                    colQ[j]++
                    isQ[i][j] = true
                }
            }
        }
    }

    val rowVal = IntArray(n)
    for (i in 0 until n) {
        rowVal[i] = rowBase[i] + rowQ[i]
    }

    val colMinBase = IntArray(m)
    for (j in 0 until m) {
        colMinBase[j] = colBase[j] - colQ[j]
    }

    var answer = Int.MIN_VALUE
    for (i in 0 until n) {
        val rv = rowVal[i]
        val rowIsQ = isQ[i]
        for (j in 0 until m) {
            var value = rv - colMinBase[j]
            if (rowIsQ[j]) {
                value -= 2
            }
            if (value > answer) {
                answer = value
            }
        }
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
