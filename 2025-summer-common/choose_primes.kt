fun solution(n: Int): Long {
    if (n < 5) return 0L

    val isPrime = BooleanArray(n + 1) { true }
    isPrime[0] = false
    if (n >= 1) isPrime[1] = false

    var i = 2
    while (i * i <= n) {
        if (isPrime[i]) {
            var j = i * i
            while (j <= n) {
                isPrime[j] = false
                j += i
            }
        }
        i++
    }

    var c1 = 0L
    var c3 = 0L
    var x = 3
    while (x <= n) {
        if (isPrime[x]) {
            val r = x % 4
            if (r == 1) c1++ else if (r == 3) c3++
        }
        x += 2
    }
    return c1 * c3
}
