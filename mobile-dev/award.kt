import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var st: StringTokenizer? = null
    fun next(): String {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            st = StringTokenizer(line)
        }
        return st!!.nextToken()
    }
    fun nextInt(): Int = next().toInt()
    fun nextLong(): Long = next().toLong()
}

private fun gcd(a: Long, b: Long): Long {
    var x = if (a >= 0) a else -a
    var y = if (b >= 0) b else -b
    while (y != 0L) {
        val t = x % y
        x = y
        y = t
    }
    return x
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)
    val n = fs.nextInt()
    val m = fs.nextLong()
    val a = LongArray(n + 1)
    for (i in 1..n) a[i] = fs.nextLong()
    val head = IntArray(n + 1) { -1 }
    val to = IntArray(n)
    val next = IntArray(n)
    var e = 0
    val sumA = LongArray(n + 1)
    val gcdA = LongArray(n + 1)
    for (i in 1..n) {
        val p = fs.nextInt()
        to[e] = i
        next[e] = head[p]
        head[p] = e
        sumA[p] += a[i]
        gcdA[p] = if (gcdA[p] == 0L) a[i] else gcd(gcdA[p], a[i])
        e++
    }
    val amount = LongArray(n + 1)
    amount[0] = m
    val q = IntArray(n + 1)
    var l = 0
    var r = 0
    q[r++] = 0
    while (l < r) {
        val v = q[l++]
        val firstEdge = head[v]
        if (firstEdge == -1) continue
        val g = gcdA[v]
        if (g == 0L) continue
        val den = sumA[v] / g
        if (den == 0L) continue
        val t = amount[v] / den
        if (t == 0L) continue
        var ei = firstEdge
        while (ei != -1) {
            val u = to[ei]
            amount[u] = t * (a[u] / g)
            q[r++] = u
            ei = next[ei]
        }
    }
    val sb = StringBuilder()
    for (i in 1..n) {
        if (i > 1) sb.append(' ')
        sb.append(amount[i])
    }
    sb.append('\n')
    writer.write(sb.toString())
    writer.flush()
    reader.close()
    writer.close()
}
