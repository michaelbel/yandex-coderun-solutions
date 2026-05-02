import java.io.BufferedInputStream

private const val MOD: Long = 1_000_000_007L

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    fun nextInt(): Int {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sign = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return if (sign == 1) res else -res
    }
}

private fun modPow(a0: Long, e0: Long): Long {
    var a = a0 % MOD
    var e = e0
    var r = 1L
    while (e > 0) {
        if ((e and 1L) == 1L) r = (r * a) % MOD
        a = (a * a) % MOD
        e = e ushr 1
    }
    return r
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    val m = n - 1

    if (m == 0) {
        print(1)
        return
    }

    val fact = LongArray(n)
    fact[0] = 1L
    for (i in 1..m) fact[i] = (fact[i - 1] * i.toLong()) % MOD

    val invFact = LongArray(n)
    invFact[m] = modPow(fact[m], MOD - 2L)
    for (i in m downTo 1) invFact[i - 1] = (invFact[i] * i.toLong()) % MOD

    var sum = 0L
    for (k in 0..m) {
        sum += (fact[k] * fact[m - k]) % MOD
        if (sum >= MOD) sum -= MOD
    }

    val sumInvC = (sum * invFact[m]) % MOD
    val pow2 = modPow(2L, m.toLong())
    val ans = (pow2 * sumInvC) % MOD

    print(ans)
}
