import java.io.BufferedInputStream
import java.util.Arrays
import kotlin.math.abs
import kotlin.math.min

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

private class DSU(n: Int) {
    private val p = IntArray(n) { it }
    private val r = IntArray(n)

    fun find(x0: Int): Int {
        var x = x0
        while (p[x] != x) {
            p[x] = p[p[x]]
            x = p[x]
        }
        return x
    }

    fun union(a0: Int, b0: Int): Boolean {
        var a = find(a0)
        var b = find(b0)
        if (a == b) return false
        if (r[a] < r[b]) {
            val t = a
            a = b
            b = t
        }
        p[b] = a
        if (r[a] == r[b]) r[a]++
        return true
    }
}

private fun sortEdgesByW(u: IntArray, v: IntArray, w: LongArray, n: Int) {
    if (n <= 1) return
    val lStack = IntArray(64)
    val rStack = IntArray(64)
    var sp = 0
    lStack[sp] = 0
    rStack[sp] = n - 1
    sp++

    while (sp > 0) {
        sp--
        var l = lStack[sp]
        var r = rStack[sp]
        while (l < r) {
            var i = l
            var j = r
            val pivot = w[(l + r) ushr 1]
            while (i <= j) {
                while (w[i] < pivot) i++
                while (w[j] > pivot) j--
                if (i <= j) {
                    val tw = w[i]; w[i] = w[j]; w[j] = tw
                    val tu = u[i]; u[i] = u[j]; u[j] = tu
                    val tv = v[i]; v[i] = v[j]; v[j] = tv
                    i++
                    j--
                }
            }
            if (j - l < r - i) {
                if (i < r) {
                    lStack[sp] = i
                    rStack[sp] = r
                    sp++
                }
                r = j
            } else {
                if (l < j) {
                    lStack[sp] = l
                    rStack[sp] = j
                    sp++
                }
                l = i
            }
        }
    }
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    if (n <= 1) {
        print(0)
        return
    }

    val x = LongArray(n)
    val y = LongArray(n)
    val z = LongArray(n)
    for (i in 0 until n) {
        x[i] = fs.nextInt().toLong()
        y[i] = fs.nextInt().toLong()
        z[i] = fs.nextInt().toLong()
    }

    val mask = 131071L
    val offset = 1_000_000_000L

    val sx = LongArray(n)
    val sy = LongArray(n)
    val sz = LongArray(n)
    for (i in 0 until n) {
        sx[i] = ((x[i] + offset) shl 17) or i.toLong()
        sy[i] = ((y[i] + offset) shl 17) or i.toLong()
        sz[i] = ((z[i] + offset) shl 17) or i.toLong()
    }

    Arrays.sort(sx)
    Arrays.sort(sy)
    Arrays.sort(sz)

    val maxEdges = 3 * (n - 1)
    val eu = IntArray(maxEdges)
    val ev = IntArray(maxEdges)
    val ew = LongArray(maxEdges)
    var eCnt = 0

    fun addEdge(a: Int, b: Int) {
        val dx = abs(x[a] - x[b])
        val dy = abs(y[a] - y[b])
        val dz = abs(z[a] - z[b])
        val cost = min(dx, min(dy, dz))
        eu[eCnt] = a
        ev[eCnt] = b
        ew[eCnt] = cost
        eCnt++
    }

    for (i in 0 until n - 1) {
        val a = (sx[i] and mask).toInt()
        val b = (sx[i + 1] and mask).toInt()
        addEdge(a, b)
    }
    for (i in 0 until n - 1) {
        val a = (sy[i] and mask).toInt()
        val b = (sy[i + 1] and mask).toInt()
        addEdge(a, b)
    }
    for (i in 0 until n - 1) {
        val a = (sz[i] and mask).toInt()
        val b = (sz[i + 1] and mask).toInt()
        addEdge(a, b)
    }

    sortEdgesByW(eu, ev, ew, eCnt)

    val dsu = DSU(n)
    var total = 0L
    var used = 0
    for (i in 0 until eCnt) {
        if (dsu.union(eu[i], ev[i])) {
            total += ew[i]
            used++
            if (used == n - 1) break
        }
    }

    print(total)
}
