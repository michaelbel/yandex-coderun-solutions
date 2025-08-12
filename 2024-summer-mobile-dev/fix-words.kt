import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val N = reader.readLine().toInt()
    val S = reader.readLine()
    val M = reader.readLine().toInt()
    val logSub = Math.log10(0.5)
    val logDel = Math.log10(0.1)
    val logIns = Math.log10(0.4)
    val thresholdLog = -10.0
    var bestLog = Double.NEGATIVE_INFINITY
    var bestWord: String? = null
    repeat(M) {
        reader.readLine()
        val W = reader.readLine()
        val m = W.length
        val n = N
        val dp = Array(m + 1) { DoubleArray(n + 1) }
        dp[0][0] = 0.0
        for (j in 1..n) {
            dp[0][j] = dp[0][j - 1] + logIns
        }
        for (i in 1..m) {
            dp[i][0] = dp[i - 1][0] + logDel
        }
        for (i in 1..m) {
            val wi = W[i - 1]
            for (j in 1..n) {
                var maxv = if (wi == S[j - 1]) {
                    dp[i - 1][j - 1]
                } else {
                    dp[i - 1][j - 1] + logSub
                }
                val delv = dp[i - 1][j] + logDel
                if (delv > maxv) maxv = delv
                val insv = dp[i][j - 1] + logIns
                if (insv > maxv) maxv = insv
                dp[i][j] = maxv
            }
        }
        val curLog = dp[m][n]
        if (curLog > thresholdLog) {
            if (curLog > bestLog + 1e-12) {
                bestLog = curLog
                bestWord = W
            } else if (Math.abs(curLog - bestLog) <= 1e-12) {
                if (bestWord == null || W < bestWord) {
                    bestWord = W
                }
            }
        }
    }
    if (bestWord == null) {
        writer.write("NO MATCH")
    } else {
        writer.write(bestWord)
    }
    writer.flush()
    reader.close()
    writer.close()
}
