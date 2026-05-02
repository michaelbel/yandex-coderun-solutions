fun solution(n: Int, m: Int, swaps: IntArray): IntArray {
    val total = 2 * n
    val at = IntArray(total + 1) { it }
    var leftMethods = n
    val result = IntArray(m)

    var idx = 0
    for (k in 0 until m) {
        val a = swaps[idx++]
        val b = swaps[idx++]

        if (a == b) {
            result[k] = leftMethods
            continue
        }

        var before = 0
        if (a <= n && at[a] <= n) before++
        if (b <= n && at[b] <= n) before++

        val tmp = at[a]
        at[a] = at[b]
        at[b] = tmp

        var after = 0
        if (a <= n && at[a] <= n) after++
        if (b <= n && at[b] <= n) after++

        leftMethods += after - before
        result[k] = leftMethods
    }

    return result
}
