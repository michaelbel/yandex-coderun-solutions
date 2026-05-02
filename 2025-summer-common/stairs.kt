fun solution(n: Int, a: IntArray): IntArray {
    val result = IntArray(n + 1)
    var prev = Int.MIN_VALUE
    for (i in 0 until n) {
        val lo = -kotlin.math.abs(a[i])
        val hi = kotlin.math.abs(a[i])
        val pick = if (lo >= prev) lo else if (hi >= prev) hi else {
            result[0] = 0
            return result
        }
        result[i + 1] = pick
        prev = pick
    }
    result[0] = 1
    return result
}
