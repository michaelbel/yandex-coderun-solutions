import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Arrays
import kotlin.math.sqrt

private class FastScanner(private val reader: BufferedReader) {
    private val buf = CharArray(1 shl 16)
    private var len = 0
    private var ptr = 0
    private fun readChar(): Int {
        if (ptr >= len) {
            len = reader.read(buf, 0, buf.size)
            ptr = 0
            if (len <= 0) return -1
        }
        return buf[ptr++].code
    }
    fun nextInt(): Int {
        var c = readChar()
        while (c <= 32 && c != -1) c = readChar()
        var sign = 1
        if (c == '-'.code) { sign = -1; c = readChar() }
        var res = 0
        while (c > 32 && c != -1) {
            res = res * 10 + (c - '0'.code)
            c = readChar()
        }
        return res * sign
    }
}

private class DSU(n: Int) {
    private val p = IntArray(n) { it }
    private val sz = IntArray(n) { 1 }
    fun find(x0: Int): Int {
        var x = x0
        while (p[x] != x) { p[x] = p[p[x]]; x = p[x] }
        return x
    }
    fun union(a0: Int, b0: Int) {
        var a = find(a0); var b = find(b0)
        if (a == b) return
        if (sz[a] < sz[b]) { val t = a; a = b; b = t }
        p[b] = a; sz[a] += sz[b]
    }
    fun same(a: Int, b: Int): Boolean = find(a) == find(b)
}

private fun floorSqrt(x: Long): Long {
    if (x <= 0L) return 0L
    var r = sqrt(x.toDouble()).toLong()
    while ((r + 1) * (r + 1) <= x) r++
    while (r * r > x) r--
    return r
}

private fun floorHalf(a: Int): Int = if (a >= 0) a / 2 else -(((-a) + 1) / 2)

private fun packEdge(t: Int, u: Int, v: Int): Long {
    return (t.toLong() shl 32) or ((u and 0xFFFF).toLong() shl 16) or ((v and 0xFFFF).toLong())
}
private fun unpackT(x: Long): Int = (x shr 32).toInt()
private fun unpackU(x: Long): Int = ((x shr 16) and 0xFFFF).toInt()
private fun unpackV(x: Long): Int = (x and 0xFFFF).toInt()

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val m = fs.nextInt()
    val w = fs.nextInt()
    val h = fs.nextInt()

    val x = IntArray(n)
    val y = IntArray(n)
    val r = IntArray(n)
    for (i in 0 until n) {
        x[i] = fs.nextInt()
        y[i] = fs.nextInt()
        r[i] = fs.nextInt()
    }

    val L = n
    val R = n + 1
    val B = n + 2
    val T = n + 3

    val totalEdges = (n * (n - 1)) / 2 + 4 * n
    val edges = LongArray(totalEdges)
    var ep = 0

    for (i in 0 until n) {
        val leftNeed = floorHalf(x[i] - r[i]) + 1
        val rightNeed = floorHalf(w - x[i] - r[i]) + 1
        val bottomNeed = floorHalf(y[i] - r[i]) + 1
        val topNeed = floorHalf(h - y[i] - r[i]) + 1
        edges[ep++] = packEdge(leftNeed, i, L)
        edges[ep++] = packEdge(rightNeed, i, R)
        edges[ep++] = packEdge(bottomNeed, i, B)
        edges[ep++] = packEdge(topNeed, i, T)
    }

    for (i in 0 until n) {
        val xi = x[i].toLong()
        val yi = y[i].toLong()
        val si = r[i].toLong()
        for (j in i + 1 until n) {
            val dx = xi - x[j].toLong()
            val dy = yi - y[j].toLong()
            val dist2 = dx * dx + dy * dy
            val dStrict = floorSqrt(dist2) + 1
            val s = si + r[j].toLong()
            val need = (dStrict - s).toLong()
            val t = if (need <= 0L) 0 else ((need + 1) / 2).toInt()
            edges[ep++] = packEdge(t, i, j)
        }
    }

    Arrays.sort(edges, 0, ep)

    val qr = IntArray(m)
    val qs = IntArray(m)
    val order = LongArray(m)
    for (i in 0 until m) {
        qr[i] = fs.nextInt()
        qs[i] = fs.nextInt()
        order[i] = (qr[i].toLong() shl 32) or (i.toLong() and 0xFFFFFFFFL)
    }
    Arrays.sort(order)

    val dsu = DSU(n + 4)
    val ans = Array(m) { "" }
    var p = 0

    fun answerFor(start: Int): String {
        val lb = dsu.same(L, B)
        val br = dsu.same(B, R)
        val rt = dsu.same(R, T)
        val tl = dsu.same(T, L)
        val lr = dsu.same(L, R)
        val bt = dsu.same(B, T)

        if ((start == 1 && lb) || (start == 2 && br) || (start == 3 && rt) || (start == 4 && tl)) {
            return start.toString()
        }

        if (lr && bt) return start.toString()
        if (lr) {
            return if (start == 1 || start == 2) "12" else "34"
        }
        if (bt) {
            return if (start == 1 || start == 4) "14" else "23"
        }

        val keep1 = !lb
        val keep2 = !br
        val keep3 = !rt
        val keep4 = !tl
        val sb = StringBuilder()
        if (keep1) sb.append('1')
        if (keep2) sb.append('2')
        if (keep3) sb.append('3')
        if (keep4) sb.append('4')
        if (sb.isEmpty()) sb.append(start.toString())
        return sb.toString()
    }

    for (k in 0 until m) {
        val key = order[k]
        val rNow = (key shr 32).toInt()
        val idx = (key and 0xFFFFFFFFL).toInt()
        while (p < ep && unpackT(edges[p]) <= rNow) {
            dsu.union(unpackU(edges[p]), unpackV(edges[p]))
            p++
        }
        ans[idx] = answerFor(qs[idx])
    }

    for (i in 0 until m) {
        writer.write(ans[i])
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
