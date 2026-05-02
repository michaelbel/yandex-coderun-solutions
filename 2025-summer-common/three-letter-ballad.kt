fun solve(ballad: String, n: Int): Long {
    val aCode = 'a'.code
    val total = IntArray(26)
    val letters = CharArray(ballad.length)
    var m = 0
    for (ch in ballad) {
        if (ch != ' ') {
            letters[m++] = ch
            total[ch.code - aCode]++
        }
    }
    if (m < 3) return 0L
    val left = IntArray(26)
    val right = total.copyOf()
    var ans = 0L
    var j = 0
    while (j < m) {
        val mid = letters[j].code - aCode
        right[mid]--
        var c = 0
        var add = 0L
        while (c < 26) {
            add += left[c].toLong() * right[c].toLong()
            c++
        }
        ans += add
        left[mid]++
        j++
    }
    return ans
}
