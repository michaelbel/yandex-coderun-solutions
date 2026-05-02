fun main() {
    val t = readln().trim().toInt()
    val out = StringBuilder()

    repeat(t) {
        val (bStr, nStr) = readln().trim().split(' ')
        val b = bStr.toLong()
        val n = nStr.toLong()

        var bestA = 0
        var bestK = Int.MAX_VALUE

        run {
            val fn = factorize(n)
            val fb = factorize(b)
            var possible = true
            var k1 = 1
            for ((p, en) in fn) {
                val eb = fb[p] ?: 0
                if (eb == 0) { possible = false; break }
                val need = (en + eb - 1) / eb
                if (need > k1) k1 = need
            }
            if (possible && k1 < bestK) { bestK = k1; bestA = 1 }
        }

        if (gcd(b, n) == 1L) {
            val ord = multiplicativeOrder(b % n, n)
            if (ord < bestK) { bestK = ord; bestA = 2 }
            if (ord % 2 == 0) {
                val half = ord / 2
                if (powmod(b % n, half, n) == (n - 1) % n) {
                    if (half < bestK || (half == bestK && (bestA == 0 || 3 < bestA))) {
                        bestK = half
                        bestA = 3
                    }
                }
            }
        }

        if (bestA == 0) out.appendLine("0") else out.appendLine("$bestA $bestK")
    }

    print(out.toString())
}

private fun gcd(a0: Long, b0: Long): Long {
    var a = kotlin.math.abs(a0)
    var b = kotlin.math.abs(b0)
    while (b != 0L) {
        val t = a % b
        a = b
        b = t
    }
    return a
}

private fun powmod(a0: Long, e0: Int, mod: Long): Long {
    var a = (a0 % mod + mod) % mod
    var e = e0
    var r = 1L
    var base = a
    while (e > 0) {
        if ((e and 1) == 1) r = (r * base) % mod
        base = (base * base) % mod
        e = e ushr 1
    }
    return r
}

private fun factorize(x0: Long): MutableMap<Long, Int> {
    var x = x0
    val res = mutableMapOf<Long, Int>()
    var d = 2L
    while (d * d <= x) {
        if (x % d == 0L) {
            var c = 0
            while (x % d == 0L) { x /= d; c++ }
            res[d] = c
        }
        d += if (d == 2L) 1 else 2
    }
    if (x > 1) res[x] = (res[x] ?: 0) + 1
    return res
}

private fun phiFromFactors(n: Long, factors: Map<Long, Int>): Long {
    var result = n
    for (p in factors.keys) {
        result = result / p * (p - 1)
    }
    return result
}

private fun multiplicativeOrder(b: Long, n: Long): Int {
    val fn = factorize(n)
    var phi = phiFromFactors(n, fn)
    val fphi = factorize(phi)
    var ord = phi.toInt()
    for ((p, _) in fphi) {
        var pp = p.toInt()
        while (ord % pp == 0 && powmod(b, ord / pp, n) == 1L) {
            ord /= pp
        }
    }
    return ord
}
