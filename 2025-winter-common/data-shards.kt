import java.io.BufferedInputStream
import java.lang.StringBuilder

private const val MOD: Long = 998244353L

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
    val t = fs.nextInt()
    val ns = IntArray(t)
    val ss = IntArray(t)
    var maxS = 0
    for (i in 0 until t) {
        val n = fs.nextInt()
        val s = fs.nextInt()
        ns[i] = n
        ss[i] = s
        if (s > maxS) maxS = s
    }

    val fact = LongArray(maxS + 1)
    val invFact = LongArray(maxS + 1)
    fact[0] = 1L
    for (i in 1..maxS) fact[i] = (fact[i - 1] * i.toLong()) % MOD
    invFact[maxS] = modPow(fact[maxS], MOD - 2L)
    for (i in maxS downTo 1) invFact[i - 1] = (invFact[i] * i.toLong()) % MOD

    fun perm(k: Int, n: Int): Long {
        if (k < n || n < 0) return 0L
        return (fact[k] * invFact[k - n]) % MOD
    }

    val out = StringBuilder(t * 12)
    for (i in 0 until t) {
        val n = ns[i]
        val s = ss[i]
        val pS = perm(s, n)
        val pS1 = perm(s - 1, n)
        var ans = ((s.toLong() + 1L) % MOD) * pS % MOD
        ans = (ans - (s.toLong() % MOD) * pS1 % MOD) % MOD
        if (ans < 0) ans += MOD
        out.append(ans).append('\n')
    }
    print(out.toString())
}
