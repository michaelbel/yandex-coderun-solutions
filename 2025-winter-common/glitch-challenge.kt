import java.io.BufferedInputStream
import java.lang.StringBuilder

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

    fun nextLong(): Long {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0L
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

private fun legendre(n: Long, p: Int): Long {
    var x = n
    var s = 0L
    val pp = p.toLong()
    while (x > 0) {
        x /= pp
        s += x
    }
    return s
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextLong()
    val k = fs.nextInt()
    val x = if (n < 1_000_000L) n.toInt() else 1_000_000

    val spf = IntArray(x + 1)
    val primes = IntArray(x + 1)
    var pc = 0
    for (i in 2..x) {
        if (spf[i] == 0) {
            spf[i] = i
            primes[pc++] = i
        }
        var j = 0
        while (j < pc) {
            val p = primes[j]
            val v = i * p
            if (v > x) break
            spf[v] = p
            if (p == spf[i]) break
            j++
        }
    }

    val expA = IntArray(x + 1)
    for (i in 0 until k) {
        var v = fs.nextInt()
        while (v > 1) {
            val p = spf[v]
            var cnt = 0
            while (v % p == 0) {
                v /= p
                cnt++
            }
            expA[p] += cnt
        }
    }

    var ans = 1L
    for (i in 0 until pc) {
        val p = primes[i]
        val e = legendre(n, p) - expA[p].toLong()
        ans = (ans * (e + 1L)) % MOD
    }

    print(ans)
}
