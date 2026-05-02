import java.io.BufferedInputStream
import java.util.Arrays

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

private fun sqMod(x: Long): Long {
    val v = x % MOD
    return (v * v) % MOD
}

private fun addMod(a: Long, b: Long): Long {
    val s = a + b
    return if (s >= MOD) s - MOD else s
}

private fun mulMod(a: Long, b: Long): Long {
    return ((a % MOD) * (b % MOD)) % MOD
}

fun main() {
    val fs = FastScanner()
    val m = fs.nextLong()
    val n = fs.nextLong().toInt()

    val w = LongArray(n)
    var sumW = 0L
    for (i in 0 until n) {
        val v = fs.nextLong()
        w[i] = v
        sumW += v
    }

    val d = sumW - m

    Arrays.sort(w)

    var idx = 0
    var prefixSum = 0L
    var prefixSq = 0L

    while (idx < n) {
        val remaining = (n - idx).toLong()
        val remDef = d - prefixSum
        val wi = w[idx]
        if (remDef >= remaining * wi) {
            prefixSum += wi
            prefixSq = addMod(prefixSq, sqMod(wi))
            idx++
        } else {
            break
        }
    }

    val remaining = (n - idx).toLong()
    val remDef = d - prefixSum

    val base = remDef / remaining
    val rem = remDef % remaining

    var ans = prefixSq
    val baseSq = sqMod(base)
    val base1Sq = sqMod(base + 1)

    ans = addMod(ans, mulMod(remaining - rem, baseSq))
    ans = addMod(ans, mulMod(rem, base1Sq))

    print(ans)
}
