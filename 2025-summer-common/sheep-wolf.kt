fun solution(n: Int, a: LongArray): Long {
    var left = 0
    var best = 0
    val freq = HashMap<Long, Int>(4)

    var right = 0
    while (right < n) {
        val x = a[right]
        freq[x] = (freq[x] ?: 0) + 1
        right++

        while (freq.size > 2) {
            val y = a[left]
            val c = freq[y]!! - 1
            if (c == 0) freq.remove(y) else freq[y] = c
            left++
        }

        if (freq.size == 2) {
            val len = right - left
            if (len > best) best = len
        }
    }
    return best.toLong()
}
