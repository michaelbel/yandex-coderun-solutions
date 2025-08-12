import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

private const val NEG_INF: Long = -1000000000000000000L

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val t = reader.readLine().toInt()
    repeat(t) {
        val (n, k) = reader.readLine().split(" ").map { it.toInt() }
        val a = reader.readLine().split(" ").map { it.toLong() }.toLongArray()
        writer.write("${solve(n, k, a)}\n")
    }
    writer.flush()
    writer.close()
    reader.close()
}

private fun solve(n: Int, k: Int, a: LongArray): Long {
    var ans = NEG_INF
    var maxA = a[0]
    for (i in 1 until n) {
        if (a[i] > maxA) maxA = a[i]
    }
    if (maxA <= 0L) return maxA
    val dp = LongArray(k + 1) { NEG_INF }
    for (i in 0 until n) {
        var r = k
        while (r >= 0) {
            if (r == 0) {
                dp[0] = maxOf(dp[0] + a[i], a[i])
            } else {
                dp[r] = maxOf(dp[r] + a[i], dp[r - 1])
            }
            if (dp[r] > ans) ans = dp[r]
            r--
        }
    }
    val pref = Array(n + 1) { LongArray(k + 1) { NEG_INF } }
    val bestPref = Array(n + 1) { LongArray(k + 1) { NEG_INF } }
    pref[0][0] = 0L
    bestPref[0][0] = 0L
    for (r in 1..k) {
        pref[0][r] = NEG_INF
        bestPref[0][r] = NEG_INF
    }
    var i = 0
    while (i < n) {
        pref[i + 1][0] = pref[i][0] + a[i]
        var r = 1
        while (r <= k) {
            pref[i + 1][r] = maxOf(pref[i][r] + a[i], pref[i][r - 1])
            r++
        }
        r = 0
        while (r <= k) {
            bestPref[i + 1][r] = bestPref[i][r]
            if (pref[i + 1][r] > bestPref[i + 1][r]) bestPref[i + 1][r] = pref[i + 1][r]
            if (r > 0 && bestPref[i + 1][r - 1] > bestPref[i + 1][r]) bestPref[i + 1][r] = bestPref[i + 1][r - 1]
            r++
        }
        i++
    }
    val suff = Array(n + 1) { LongArray(k + 1) { NEG_INF } }
    val bestSuff = Array(n + 1) { LongArray(k + 1) { NEG_INF } }
    suff[n][0] = 0L
    bestSuff[n][0] = 0L
    for (r in 1..k) {
        suff[n][r] = NEG_INF
        bestSuff[n][r] = NEG_INF
    }
    i = n - 1
    while (i >= 0) {
        suff[i][0] = suff[i + 1][0] + a[i]
        var r = 1
        while (r <= k) {
            suff[i][r] = maxOf(suff[i + 1][r] + a[i], suff[i + 1][r - 1])
            r++
        }
        r = 0
        while (r <= k) {
            bestSuff[i][r] = bestSuff[i + 1][r]
            if (suff[i][r] > bestSuff[i][r]) bestSuff[i][r] = suff[i][r]
            if (r > 0 && bestSuff[i][r - 1] > bestSuff[i][r]) bestSuff[i][r] = bestSuff[i][r - 1]
            r++
        }
        i--
    }
    i = 1
    while (i < n) {
        var p = 0
        while (p <= k) {
            val cand = bestPref[i][p] + bestSuff[i][k - p]
            if (cand > ans) ans = cand
            p++
        }
        i++
    }
    return ans
}
