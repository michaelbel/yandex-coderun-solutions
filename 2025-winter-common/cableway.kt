import java.io.BufferedInputStream
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

private const val MOD: Long = 998244353L
private const val MAXN: Int = 3000

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

fun main() {
    val stride = MAXN + 1
    val dp = IntArray((MAXN + 1) * stride)
    dp[0] = 1
    for (n in 1..MAXN) {
        val prev = (n - 1) * stride
        val cur = n * stride
        dp[cur] = 0
        val mul = (n - 1).toLong()
        for (k in 1..n) {
            val v = (dp[prev + (k - 1)].toLong() + mul * dp[prev + k].toLong()) % MOD
            dp[cur + k] = v.toInt()
        }
    }

    val fs = FastScanner()
    val t = fs.nextInt()
    val out = StringBuilder(t * 8)

    for (caseIdx in 0 until t) {
        val n = fs.nextInt()
        val q = fs.nextInt()
        val l = fs.nextInt()
        val r = fs.nextInt()

        val b = IntArray(q)
        for (i in 0 until q) b[i] = fs.nextInt()

        val to = IntArray(n + 1)
        val from = IntArray(n + 1)

        var ok = true
        for (i in 0 until q) {
            val bj = b[i]
            val cj = fs.nextInt()
            if (bj < 1 || bj > n || cj < 1 || cj > n) {
                ok = false
                continue
            }
            if (to[bj] != 0 || from[cj] != 0) {
                ok = false
                continue
            }
            to[bj] = cj
            from[cj] = bj
        }

        if (!ok) {
            out.append('0').append('\n')
            continue
        }

        var kComp = 0
        for (i in 1..n) if (from[i] == 0) kComp++

        var fixedCycles = 0
        val state = ByteArray(n + 1)
        for (i in 1..n) {
            if (to[i] == 0 || state[i].toInt() != 0) continue
            var cur = i
            while (cur != 0 && state[cur].toInt() == 0) {
                state[cur] = 1
                cur = to[cur]
            }
            if (cur != 0 && state[cur].toInt() == 1) fixedCycles++
            cur = i
            while (cur != 0 && state[cur].toInt() == 1) {
                state[cur] = 2
                cur = to[cur]
            }
        }

        val lo = max(0, l - fixedCycles)
        val hi = min(kComp, r - fixedCycles)
        if (lo > hi) {
            out.append('0').append('\n')
            continue
        }

        val base = kComp * stride
        var ans = 0L
        for (c in lo..hi) {
            ans += dp[base + c].toLong()
            if (ans >= MOD) ans -= MOD
        }
        out.append(ans).append('\n')
    }

    print(out.toString())
}
