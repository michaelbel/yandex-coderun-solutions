import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.Locale

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
    fun nextDouble(): Double = next().toDouble()
}

private data class Interval(
    val start: Double,
    val end: Double,
    val w: Double
)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    val n = scanner.nextInt()
    if (n == 0) {
        writer.write("0.0")
        writer.newLine()
        writer.flush()
        return
    }

    val intervals = ArrayList<Interval>(n)
    repeat(n) {
        val b = scanner.nextDouble()
        val e = scanner.nextDouble()
        val w = scanner.nextDouble()
        intervals.add(Interval(b, e, w))
    }

    // Сортируем по времени окончания
    intervals.sortBy { it.end }

    // Делаем индексацию с 1 для удобства DP
    val starts = DoubleArray(n + 1)
    val ends = DoubleArray(n + 1)
    val weights = DoubleArray(n + 1)

    for (i in 1..n) {
        val iv = intervals[i - 1]
        starts[i] = iv.start
        ends[i] = iv.end
        weights[i] = iv.w
    }

    val dp = DoubleArray(n + 1)
    dp[0] = 0.0

    val eps = 1e-12

    for (i in 1..n) {
        // бинпоиск последнего j < i с ends[j] <= starts[i]
        var l = 1
        var r = i - 1
        var best = 0
        while (l <= r) {
            val mid = (l + r) ushr 1
            if (ends[mid] <= starts[i] + eps) {
                best = mid
                l = mid + 1
            } else {
                r = mid - 1
            }
        }

        val take = dp[best] + weights[i]
        val skip = dp[i - 1]
        dp[i] = if (take > skip) take else skip
    }

    writer.write(String.format(Locale.US, "%.10f", dp[n]))
    writer.newLine()
    writer.flush()
}
