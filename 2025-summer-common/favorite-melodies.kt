fun solution(n: Int, q: Int, a: LongArray, queries: Array<IntArray>): Long {
    if (n == 0 || q == 0) return 0L

    val diff = LongArray(n + 1)
    for (qr in queries) {
        val l = qr[0] - 1
        val r = qr[1] - 1
        diff[l] += 1L
        diff[r + 1] -= 1L
    }

    val freq = LongArray(n)
    var cur = 0L
    for (i in 0 until n) {
        cur += diff[i]
        freq[i] = cur
    }

    a.sort()
    freq.sort()

    var ans = 0L
    for (i in 0 until n) {
        if (freq[i] == 0L) continue
        ans += a[i] * freq[i]
    }
    return ans
}
