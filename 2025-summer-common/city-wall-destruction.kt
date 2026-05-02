fun solve(n: Int, a: LongArray): Long {
    val prevG = IntArray(n)
    val nextG = IntArray(n)
    val prevS = IntArray(n)
    val nextS = IntArray(n)
    val st = IntArray(n)
    var sz = 0

    for (i in 0 until n) {
        while (sz > 0 && a[st[sz - 1]] <= a[i]) sz--
        prevG[i] = if (sz == 0) -1 else st[sz - 1]
        st[sz++] = i
    }
    sz = 0
    for (i in n - 1 downTo 0) {
        while (sz > 0 && a[st[sz - 1]] < a[i]) sz--
        nextG[i] = if (sz == 0) n else st[sz - 1]
        st[sz++] = i
    }

    sz = 0
    for (i in 0 until n) {
        while (sz > 0 && a[st[sz - 1]] >= a[i]) sz--
        prevS[i] = if (sz == 0) -1 else st[sz - 1]
        st[sz++] = i
    }
    sz = 0
    for (i in n - 1 downTo 0) {
        while (sz > 0 && a[st[sz - 1]] > a[i]) sz--
        nextS[i] = if (sz == 0) n else st[sz - 1]
        st[sz++] = i
    }

    var ans = 0L
    for (i in 0 until n) {
        val leftMax = i - prevG[i]
        val rightMax = nextG[i] - i
        val leftMin = i - prevS[i]
        val rightMin = nextS[i] - i
        ans += a[i] * (leftMax.toLong() * rightMax.toLong() - leftMin.toLong() * rightMin.toLong())
    }
    return ans
}
