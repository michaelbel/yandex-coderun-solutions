fun solve(n: Int, m: Int): Long {
    val nn = n.toLong()
    val mm = m.toLong()
    if (nn == 0L) return 1L

    fun g(k: Long): Long {
        val target = mm + 1L
        if (k <= nn) {
            val res = 2L + (nn - 1L) * k
            return if (res >= target) target else res
        } else {
            var leftSum = 0L
            var c = 1L
            var i = 0L
            val half = nn / 2L
            var L = -1L
            if (c < k) leftSum += 1L else L = 0L
            while (L == -1L && i < half) {
                val num = nn - i
                val den = i + 1L
                c = (c * num) / den
                i += 1L
                if (c >= k) {
                    L = i
                    break
                } else {
                    leftSum += c
                    if (leftSum >= target) return target
                }
            }
            if (L == -1L) return target
            val middleLevels = nn - 2L * L + 1L
            var total = 2L * leftSum
            if (middleLevels > 0L) {
                val add = middleLevels * k
                total += add
            }
            return if (total >= target) target else total
        }
    }

    var lo = 1L
    var hi = mm
    var ans = mm
    while (lo <= hi) {
        val mid = (lo + hi) ushr 1
        if (g(mid) >= mm) {
            ans = mid
            hi = mid - 1
        } else {
            lo = mid + 1
        }
    }
    return ans
}
