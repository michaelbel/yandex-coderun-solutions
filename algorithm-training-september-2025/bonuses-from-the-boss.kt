import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var st: StringTokenizer? = null

    fun nextInt(): Int = nextLong().toInt()

    fun nextLong(): Long {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return 0L
            st = StringTokenizer(line)
        }
        return st!!.nextToken().toLong()
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val a = LongArray(n + 1)
    for (i in 1..n) {
        a[i] = fs.nextLong()
    }

    // difference array for number of active j's
    val diff = LongArray(n + 3)

    for (j in 1..n) {
        val aj = a[j]
        if (aj >= 2L) {
            val start = j + 1
            var end = j + aj.toInt() - 1
            if (start <= n) {
                if (end > n) end = n
                if (start <= end) {
                    diff[start] += 1L
                    diff[end + 1] -= 1L
                }
            }
        }
    }

    var active = 0L
    var answer = 0L
    for (i in 1..n) {
        active += diff[i]
        answer += active * a[i]
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
