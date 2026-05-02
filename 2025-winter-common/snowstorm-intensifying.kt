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
        do { c = readByte() } while (c <= 32 && c >= 0)
        var sgn = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sgn = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return res * sgn
    }

    fun nextString(): String {
        var c: Int
        do { c = readByte() } while (c <= 32 && c >= 0)
        val sb = StringBuilder()
        var ch = c
        while (ch > 32 && ch >= 0) {
            sb.append(ch.toChar())
            ch = readByte()
        }
        return sb.toString()
    }
}

private class IntLongHashMap(initialCapacity: Int) {
    private val emptyKey = Int.MIN_VALUE
    private var mask: Int
    private var keys: IntArray
    private var values: LongArray
    private var size = 0
    private var rehashAt: Int

    init {
        var cap = 1
        while (cap < initialCapacity) cap = cap shl 1
        mask = cap - 1
        keys = IntArray(cap) { emptyKey }
        values = LongArray(cap)
        rehashAt = (cap * 7) / 10
    }

    private fun mix(x: Int): Int {
        var h = x * -0x7a143595
        h = h xor (h ushr 16)
        return h
    }

    fun get(key: Int): Long {
        var idx = mix(key) and mask
        while (true) {
            val k = keys[idx]
            if (k == emptyKey) return Long.MIN_VALUE
            if (k == key) return values[idx]
            idx = (idx + 1) and mask
        }
    }

    private fun putInternal(key: Int, value: Long) {
        var idx = mix(key) and mask
        while (true) {
            val k = keys[idx]
            if (k == emptyKey) {
                keys[idx] = key
                values[idx] = value
                size++
                return
            }
            if (k == key) {
                values[idx] = value
                return
            }
            idx = (idx + 1) and mask
        }
    }

    private fun rehash() {
        val oldKeys = keys
        val oldValues = values
        val newCap = oldKeys.size shl 1
        mask = newCap - 1
        keys = IntArray(newCap) { emptyKey }
        values = LongArray(newCap)
        size = 0
        rehashAt = (newCap * 7) / 10
        for (i in oldKeys.indices) {
            val k = oldKeys[i]
            if (k != emptyKey) putInternal(k, oldValues[i])
        }
    }

    fun put(key: Int, value: Long) {
        if (size >= rehashAt) rehash()
        putInternal(key, value)
    }
}

private fun pack(ways: Long, sum: Long): Long {
    return (ways shl 32) or (sum and 0xffffffffL)
}

private fun waysFromPacked(v: Long): Long = v ushr 32
private fun sumFromPacked(v: Long): Long = v and 0xffffffffL

fun main() {
    val fs = FastScanner()
    val T = fs.nextInt()
    val out = StringBuilder()

    repeat(T) {
        val s = fs.nextString()
        val t = fs.nextString()
        val n = s.length
        val m = t.length

        val maxNodes = 1 + m * (m + 1) / 2
        val child = IntArray(maxNodes * 26) { -1 }
        val occ = LongArray(maxNodes)
        var nodes = 1

        val tChars = t.toCharArray()
        for (i in 0 until m) {
            var v = 0
            for (j in i until m) {
                val c = tChars[j].code - 'a'.code
                val idx = v * 26 + c
                var nx = child[idx]
                if (nx == -1) {
                    nx = nodes++
                    child[idx] = nx
                }
                v = nx
                occ[v]++
            }
        }

        val subSum = LongArray(nodes)
        for (v in nodes - 1 downTo 0) {
            var sum = occ[v] % MOD
            val base = v * 26
            var c = 0
            while (c < 26) {
                val nx = child[base + c]
                if (nx != -1) {
                    sum += subSum[nx]
                    if (sum >= (1L shl 62)) sum %= MOD
                }
                c++
            }
            subSum[v] = sum % MOD
        }

        val lessAdd = LongArray(nodes * 26)
        for (v in 0 until nodes) {
            var pref = 0L
            val base = v * 26
            var c = 0
            while (c < 26) {
                lessAdd[base + c] = pref
                val nx = child[base + c]
                if (nx != -1) {
                    pref += subSum[nx]
                    if (pref >= (1L shl 62)) pref %= MOD
                }
                c++
            }
        }

        val pow2 = LongArray(n + 1)
        pow2[0] = 1L
        for (i in 1..n) pow2[i] = (pow2[i - 1] * 2L) % MOD

        fun deadWays(r: Int): Long {
            return if (r == 0) 1L else pow2[r - 1]
        }

        val sChars = s.toCharArray()
        val shift = 17
        val memo = IntLongHashMap(1 shl 20)

        fun solve(r: Int, u: Int): Long {
            if (r == 0) return pack(1L, 0L)
            if (u == -1) return pack(deadWays(r), 0L)

            val key = (r shl shift) or u
            val got = memo.get(key)
            if (got != Long.MIN_VALUE) return got

            var ways = 0L
            var sum = 0L

            var l = 0
            while (l < r) {
                var node = u
                var delta = 0L
                var i = l
                while (i < r) {
                    if (node == -1) break
                    val ch = sChars[i].code - 'a'.code
                    val base = node * 26 + ch
                    delta += lessAdd[base]
                    if (delta >= (1L shl 62)) delta %= MOD
                    val nx = child[base]
                    if (nx == -1) {
                        node = -1
                        break
                    } else {
                        delta += (occ[nx] % MOD)
                        if (delta >= (1L shl 62)) delta %= MOD
                        node = nx
                    }
                    i++
                }
                delta %= MOD

                val res = solve(l, node)
                val w2 = waysFromPacked(res)
                val s2 = sumFromPacked(res)

                ways += w2
                if (ways >= MOD) ways %= MOD

                var add = (w2 * delta) % MOD
                add += s2
                add %= MOD
                sum += add
                if (sum >= MOD) sum %= MOD

                l++
            }

            val packed = pack(ways % MOD, sum % MOD)
            memo.put(key, packed)
            return packed
        }

        val totalSub = ((m.toLong() * (m + 1L) / 2L) % MOD)
        val totalPartitions = if (n == 0) 1L else pow2[n - 1]

        val resRoot = solve(n, 0)
        val sumLeq = sumFromPacked(resRoot)

        var ans = (totalPartitions * totalSub) % MOD
        ans = (ans - sumLeq) % MOD
        if (ans < 0) ans += MOD

        out.append(ans).append('\n')
    }

    print(out.toString())
}
