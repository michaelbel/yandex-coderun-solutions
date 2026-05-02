fun solution(n: Int, a: IntArray): Int {
    if (n <= 1) return 0
    a.sort()
    var i = 0
    var maxInWindow = 1
    for (j in 0 until n) {
        while (a[j].toLong() - a[i].toLong() > (n - 1).toLong()) {
            i++
        }
        val window = j - i + 1
        if (window > maxInWindow) maxInWindow = window
    }
    return n - maxInWindow
}
