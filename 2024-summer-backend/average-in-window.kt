import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.StringTokenizer
import java.util.Locale

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    val k = st.nextToken().toInt()
    val a = IntArray(n)
    val st2 = StringTokenizer(reader.readLine())
    var maxA = 0
    for (i in 0 until n) {
        val v = st2.nextToken().toInt()
        a[i] = v
        if (v > maxA) maxA = v
    }

    val prefix = DoubleArray(n + 1)

    fun check(mid: Double): Boolean {
        prefix[0] = 0.0
        for (i in 0 until n) {
            prefix[i + 1] = prefix[i] + a[i] - mid
        }
        var minPrefix = 0.0
        for (i in k..n) {
            minPrefix = minOf(minPrefix, prefix[i - k])
            if (prefix[i] - minPrefix >= 0.0) return true
        }
        return false
    }

    var low = 0.0
    var high = maxA.toDouble()
    repeat(60) {
        val mid = (low + high) / 2
        if (check(mid)) low = mid else high = mid
    }

    val writer = PrintWriter(System.out)
    writer.format(Locale.US, "%.6f\n", low)
    writer.flush()
}
