import java.io.BufferedInputStream
import kotlin.math.abs

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

private fun modNorm(x: Long): Long {
    val r = x % MOD
    return if (r < 0) r + MOD else r
}

private fun modMul(a: Long, b: Long): Long = (a % MOD) * (b % MOD) % MOD

private fun modPow(a: Long, e: Long): Long {
    var base = modNorm(a)
    var exp = e
    var res = 1L
    while (exp > 0) {
        if ((exp and 1L) == 1L) res = modMul(res, base)
        base = modMul(base, base)
        exp = exp ushr 1
    }
    return res
}

private val inv2: Long = modPow(2L, MOD - 2L)
private val inv6: Long = modPow(6L, MOD - 2L)

private fun sumRange(l: Long, r: Long): Long {
    if (l > r) return 0L
    val cnt = modNorm(r - l + 1L)
    val lr = modNorm(l + r)
    return modMul(modMul(lr, cnt), inv2)
}

private fun sumSqUpTo(n: Long): Long {
    if (n <= 0L) return 0L
    val a = modNorm(n)
    val b = modNorm(n + 1L)
    val c = modNorm(2L * n + 1L)
    return modMul(modMul(modMul(a, b), c), inv6)
}

private fun sumSqRange(l: Long, r: Long): Long {
    if (l > r) return 0L
    val res = sumSqUpTo(r) - sumSqUpTo(l - 1L)
    return if (res < 0) res + MOD else res
}

private fun calcBase(N: Long): Long {
    if (N <= 1L) return 0L
    val nMod = modNorm(N)
    val eq = modMul(modMul(nMod, nMod), modNorm(N - 1L))

    val m = N - 1L
    val n1Mod = modNorm(N + 1L)

    var s = 0L
    var i = 1L
    while (i <= m) {
        val v = m / i
        val r = m / v
        val l = i

        val cnt = modNorm(r - l + 1L)
        val sumE = sumRange(l, r)
        val sumE2 = sumSqRange(l, r)

        val a = modNorm(v)
        val ap1 = modNorm(v + 1L)
        val aa1 = modMul(a, ap1)

        val const = modMul(modMul(a, nMod), n1Mod)
        val termConst = modMul(cnt, const)

        var coeffE = (MOD - modMul(aa1, nMod)) % MOD
        coeffE = (coeffE + MOD - modMul(aa1, inv2)) % MOD

        val coeffE2 = modMul(modMul(aa1, modNorm(2L * v + 1L)), inv6)

        var term = termConst
        term = (term + modMul(sumE, coeffE)) % MOD
        term = (term + modMul(sumE2, coeffE2)) % MOD

        s += term
        s %= MOD

        i = r + 1L
    }

    val nonEq = modMul(2L, s)
    return (eq + nonEq) % MOD
}

private fun calcExtraMinus2(N: Long): Long {
    if (N < 3L) return 0L

    val sd1 = modMul(modMul(modNorm(N - 2L), modNorm(N + 1L)), inv2)

    val maxX = N - 3L
    if (maxX < 1L) return modMul(4L, sd1)

    val start = if ((N and 1L) == 0L) 1L else 2L
    if (start > maxX) return modMul(4L, sd1)

    val k = (maxX - start) / 2L
    val kMod = modNorm(k)
    val k1Mod = modNorm(k + 1L)

    val sumT = modMul(modMul(kMod, k1Mod), inv2)
    val sumT2 = modMul(modMul(modMul(kMod, k1Mod), modNorm(2L * k + 1L)), inv6)

    val startMod = modNorm(start)
    val start2Mod = modMul(startMod, startMod)

    val sumX = (modMul(k1Mod, startMod) + modMul(2L, sumT)) % MOD
    var sumX2 = modMul(k1Mod, start2Mod)
    sumX2 = (sumX2 + modMul(modMul(modMul(4L, startMod), sumT), 1L)) % MOD
    sumX2 = (sumX2 + modMul(4L, sumT2)) % MOD

    val s2 = modMul((sumX2 + sumX) % MOD, inv2)

    return modMul(4L, (sd1 + s2) % MOD)
}

fun main() {
    val fs = FastScanner()
    val a = fs.nextLong()
    val q = fs.nextLong()
    val l = fs.nextLong()
    val r = fs.nextLong()

    if (a == 0L) {
        print(0)
        return
    }

    val n = r - l + 1L
    if (n <= 1L) {
        print(0)
        return
    }

    if (q == 1L) {
        print(0)
        return
    }

    if (q == -1L) {
        val even = (n + 1L) / 2L
        val odd = n / 2L
        val ks = modMul(2L, modMul(modNorm(even), modNorm(odd)))
        val ans = modMul(modMul(modNorm(n), modNorm(n)), ks)
        print(ans)
        return
    }

    if (abs(q) <= 1L) {
        print(0)
        return
    }

    var ans = calcBase(n)
    if (q == -2L) {
        ans = (ans + calcExtraMinus2(n)) % MOD
    }
    print(ans)
}
