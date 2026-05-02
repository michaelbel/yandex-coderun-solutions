import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    fun nextInt(): Int = next().toInt()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    val n = scanner.nextInt()
    val m = scanner.nextInt()
    val total = n * m

    val a = Array(n) { IntArray(m) }
    var maxVal = 0

    for (i in 0 until n) {
        for (j in 0 until m) {
            val v = scanner.nextInt()
            a[i][j] = v
            if (v > maxVal) maxVal = v
        }
    }

    // buckets: for каждого значения храним односвязный список ячеек с этим значением
    val head = IntArray(maxVal + 1) { -1 }
    val next = IntArray(total)

    var id = 0
    for (i in 0 until n) {
        for (j in 0 until m) {
            val v = a[i][j]
            next[id] = head[v]
            head[v] = id
            id++
        }
    }

    val dp = IntArray(total)
    var answer = 0

    // обходим значения по убыванию: для v используем уже посчитанные длины для v+1
    for (v in maxVal downTo 1) {
        var cell = head[v]
        while (cell != -1) {
            val r = cell / m
            val c = cell - r * m

            var bestNext = 0

            // вверх
            if (r > 0 && a[r - 1][c] == v + 1) {
                val cand = dp[cell - m]
                if (cand > bestNext) bestNext = cand
            }
            // вниз
            if (r + 1 < n && a[r + 1][c] == v + 1) {
                val cand = dp[cell + m]
                if (cand > bestNext) bestNext = cand
            }
            // влево
            if (c > 0 && a[r][c - 1] == v + 1) {
                val cand = dp[cell - 1]
                if (cand > bestNext) bestNext = cand
            }
            // вправо
            if (c + 1 < m && a[r][c + 1] == v + 1) {
                val cand = dp[cell + 1]
                if (cand > bestNext) bestNext = cand
            }

            val len = bestNext + 1
            dp[cell] = len
            if (len > answer) answer = len

            cell = next[cell]
        }
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
