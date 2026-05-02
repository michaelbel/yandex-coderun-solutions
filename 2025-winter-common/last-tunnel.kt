import java.io.BufferedInputStream

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

    fun nextInt(): Int = nextLong().toInt()
}

private fun matMul(a: LongArray, b: LongArray, n: Int): LongArray {
    val c = LongArray(n * n)
    var i = 0
    while (i < n) {
        val row = i * n
        var k = 0
        while (k < n) {
            val av = a[row + k]
            if (av != 0L) {
                val bk = k * n
                var j = 0
                while (j < n) {
                    c[row + j] = (c[row + j] + av * b[bk + j]) % MOD
                    j++
                }
            }
            k++
        }
        i++
    }
    return c
}

private fun matVecMul(a: LongArray, v: LongArray, n: Int): LongArray {
    val r = LongArray(n)
    var i = 0
    while (i < n) {
        val row = i * n
        var s = 0L
        var j = 0
        while (j < n) {
            s += a[row + j] * v[j] % MOD
            if (s >= MOD) s -= MOD
            j++
        }
        r[i] = s
        i++
    }
    return r
}

private fun matPowApply(mat: LongArray, power: Long, vec: LongArray, n: Int): LongArray {
    var p = power
    var base = mat
    var v = vec
    var first = true
    while (p > 0) {
        if ((p and 1L) == 1L) {
            v = if (first) {
                first = false
                matVecMul(base, v, n)
            } else {
                matVecMul(base, v, n)
            }
        }
        p = p ushr 1
        if (p > 0) base = matMul(base, base, n)
    }
    return v
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextLong()
    val m = fs.nextInt()
    val good = BooleanArray(28)
    for (i in 0 until m) {
        val v = fs.nextInt()
        if (v in 0..27) good[v] = true
    }

    val states = 100
    val trans = LongArray(states * states)

    var a = 0
    while (a <= 9) {
        var b = 0
        while (b <= 9) {
            val from = a * 10 + b
            var c = 0
            while (c <= 9) {
                if (good[a + b + c]) {
                    val to = b * 10 + c
                    trans[to * states + from] = (trans[to * states + from] + 1L) % MOD
                }
                c++
            }
            b++
        }
        a++
    }

    val init = LongArray(states)
    var first = 1
    while (first <= 9) {
        var second = 0
        while (second <= 9) {
            init[first * 10 + second] = (init[first * 10 + second] + 1L) % MOD
            second++
        }
        first++
    }

    val steps = n - 2L
    val vec = if (steps == 0L) init else matPowApply(trans, steps, init, states)

    var ans = 0L
    var i = 0
    while (i < states) {
        ans += vec[i]
        ans %= MOD
        i++
    }
    print(ans)
}
