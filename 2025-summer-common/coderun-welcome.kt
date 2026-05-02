fun solve(n: Int, m: Int): Int {
    val a = minOf(n.toLong(), m.toLong())
    val sum = n.toLong() + m.toLong()

    fun isOk(k: Long): Boolean {
        val sq = k * k
        if (sq > sum) return false
        return (sq / 2L) <= a
    }

    fun longSqrt(x: Long): Long {
        if (x <= 0L) return 0L
        var r = kotlin.math.sqrt(x.toDouble()).toLong()
        while ((r + 1) * (r + 1) <= x) r++
        while (r * r > x) r--
        return r
    }

    var lo = 0L
    var hi = longSqrt(sum) + 1L
    while (lo < hi) {
        val mid = (lo + hi + 1L) / 2L
        if (isOk(mid)) lo = mid else hi = mid - 1L
        }
    return lo.toInt()
}
