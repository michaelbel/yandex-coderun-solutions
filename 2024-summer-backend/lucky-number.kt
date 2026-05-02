import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val x = reader.readLine().trim()
    val n = x.length
    val k = n / 2
    val digits = IntArray(n) { x[it] - '0' }
    val pre = IntArray(n + 1)
    for (i in 0 until n) {
        pre[i + 1] = pre[i] + digits[i]
    }

    val y = IntArray(n)
    var found = false

    for (p in n - 1 downTo 0) {
        val orig = digits[p]
        for (d in orig + 1..9) {
            val sum1Fixed = if (p < k) pre[p] + d else pre[k]
            val sum2Fixed = if (p < k) 0 else (pre[p] - pre[k] + d)
            val suffix1 = if (p < k - 1) (k - p - 1) else 0
            val suffix2 = if (p + 1 <= k) (n - k) else (n - p - 1)
            val diffInit = sum1Fixed - sum2Fixed
            if (suffix1 == 0 && (diffInit < 0 || diffInit > suffix2 * 9)) continue
            for (i in 0 until p) {
                y[i] = digits[i]
            }
            y[p] = d
            var diff = diffInit
            var rem1 = suffix1
            var rem2 = suffix2
            for (i in p + 1 until n) {
                if (i < k) {
                    rem1--
                    val maxDiff = rem2 * 9
                    val minDiff = -rem1 * 9
                    val lo = (minDiff - diff).coerceAtLeast(0)
                    y[i] = lo
                    diff += lo
                } else {
                    rem2--
                    val maxDiff = rem2 * 9
                    val minDiff = -rem1 * 9
                    val lo = (diff - maxDiff).coerceAtLeast(0)
                    y[i] = lo
                    diff -= lo
                }
            }
            val sb = StringBuilder(n)
            for (dgt in y) sb.append(dgt)
            writer.write(sb.toString())
            writer.newLine()
            found = true
            break
        }
        if (found) break
    }

    if (!found) {
        val sb = StringBuilder(n)
        for (i in 0 until k - 1) sb.append('0')
        sb.append('1')
        for (i in k until n - 1) sb.append('0')
        sb.append('1')
        writer.write(sb.toString())
        writer.newLine()
    }

    writer.flush()
    writer.close()
    reader.close()
}
