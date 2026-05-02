fun solution(n: Int, t: Int, a: LongArray, b: LongArray): LongArray {
    val res = LongArray(t + 1)
    var total = 0L
    for (i in 0 until n) total += a[i]
    res[0] = total
    if (t == 0) return res

    val diffB = LongArray(t + 3)
    val rem = LongArray(t + 2)

    for (i in 0 until n) {
        val ai = a[i]
        val bi = b[i]
        if (bi == 0L) continue
        val m = if (ai == 0L) 0L else (ai + bi - 1) / bi

        var end = m - 1
        if (end > t.toLong()) end = t.toLong()
        if (end >= 1L) {
            diffB[1] += bi
            diffB[(end + 1).toInt()] -= bi
        }
        if (m >= 1L && m <= t.toLong()) {
            rem[m.toInt()] += ai - (m - 1) * bi
        }
    }

    var curB = 0L
    var decSum = 0L
    for (k in 1..t) {
        curB += diffB[k]
        decSum += curB + rem[k]
        res[k] = total - decSum
    }
    return res
}
