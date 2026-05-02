import java.io.BufferedInputStream
import java.lang.StringBuilder
import java.util.Arrays

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
        do c = readByte() while (c <= 32 && c >= 0)
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

private class DSU(n: Int) {
    private val parent = IntArray(n + 1) { it }
    private val size = IntArray(n + 1) { 1 }

    fun find(x0: Int): Int {
        var x = x0
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]
            x = parent[x]
        }
        return x
    }

    fun union(a0: Int, b0: Int): Int {
        var a = find(a0)
        var b = find(b0)
        if (a == b) return size[a]
        if (size[a] < size[b]) {
            val t = a
            a = b
            b = t
        }
        parent[b] = a
        size[a] += size[b]
        return size[a]
    }
}

fun main() {
    val fs = FastScanner()
    val t = fs.nextInt()
    val out = StringBuilder()

    val IDX_BITS = 20
    val IDX_MASK = (1L shl IDX_BITS) - 1L

    repeat(t) {
        val n = fs.nextInt()
        val m = fs.nextInt()

        val a = IntArray(m)
        val b = IntArray(m)
        val c = IntArray(m)

        for (i in 0 until m) a[i] = fs.nextInt()
        for (i in 0 until m) b[i] = fs.nextInt()
        for (i in 0 until m) c[i] = fs.nextInt()

        val keys = LongArray(m)
        for (i in 0 until m) {
            keys[i] = (c[i].toLong() shl IDX_BITS) or i.toLong()
        }
        Arrays.sort(keys)

        val dsu = DSU(n)
        val ans = LongArray(n + 1) { -1L }
        ans[1] = 0L
        var filled = 1
        var maxSize = 1

        var i = 0
        while (i < m) {
            val w = (keys[i] ushr IDX_BITS).toInt()
            while (i < m && ((keys[i] ushr IDX_BITS).toInt() == w)) {
                val idx = (keys[i] and IDX_MASK).toInt()
                val u = a[idx]
                val v = b[idx]
                val sz = dsu.union(u, v)
                if (sz > maxSize) maxSize = sz
                i++
            }
            if (maxSize > filled) {
                var k = filled + 1
                while (k <= maxSize) {
                    ans[k] = w.toLong()
                    k++
                }
                filled = maxSize
            }
        }

        var k = 1
        while (k <= n) {
            if (k > 1) out.append(' ')
            out.append(ans[k])
            k++
        }
        out.append('\n')
    }

    print(out.toString())
}
