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
    val m = fs.nextInt()
    val a = IntArray(m + 1)
    var n = 0
    var maxIdx = 0
    for (i in 1..m) {
        val v = fs.nextInt()
        a[i] = v
        if (v > 0) maxIdx = i
        n += v
    }

    if (n <= 1) {
        print(0)
        return
    }

    val h0 = a[maxIdx]
    if (h0 == n) {
        print(0)
        return
    }

    val inv = LongArray(n + 1)
    inv[1] = 1L
    for (i in 2..n) {
        inv[i] = (MOD - (MOD / i.toLong()) * inv[(MOD % i.toLong()).toInt()] % MOD) % MOD
    }

    var sum = 0L
    val nn = n
    for (h in h0 until nn) {
        val term = (inv[h] * inv[nn - h]) % MOD
        sum += term
        if (sum >= MOD) sum -= MOD
    }

    val inv2 = (MOD + 1L) / 2L
    val comb = (((n.toLong() * (n - 1L)) % MOD) * inv2) % MOD
    val ans = (comb * sum) % MOD
    print(ans)
}
