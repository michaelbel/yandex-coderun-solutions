fun solve(n: Int, k: Int, a: IntArray): IntArray {
    val allowed = BooleanArray(n + 1)
    allowed[0] = true
    allowed[n] = true
    for (pos in a) {
        if (pos in 1..n) allowed[pos] = true
    }

    val inf = 1_000_000_000
    val dp = IntArray(n + 1) { inf }
    dp[n] = 0

    for (i in n - 1 downTo 0) {
        if (!allowed[i]) continue
        var best = inf
        if (i + 1 <= n && allowed[i + 1]) {
            val cand = 1 + dp[i + 1]
            if (cand < best) best = cand
        }
        if (i + 2 <= n && allowed[i + 2]) {
            val cand = 1 + dp[i + 2]
            if (cand < best) best = cand
        }
        dp[i] = best
    }

    if (dp[0] >= inf) return intArrayOf(-1)

    val ansLen = dp[0]
    val res = IntArray(ansLen + 1)
    res[0] = ansLen

    var idx = 1
    var pos = 0
    while (pos < n) {
        val canStep1 = pos + 1 <= n && allowed[pos + 1] && dp[pos] == 1 + dp[pos + 1]
        if (canStep1) {
            res[idx++] = 1
            pos += 1
        } else {
            res[idx++] = 2
            pos += 2
        }
    }
    return res
}
