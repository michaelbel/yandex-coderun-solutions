import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class SegmentTree(n: Int) {
    val size: Int
    val seg: LongArray
    val lazy: LongArray
    init {
        var s = 1
        while (s < n) {
            s *= 2
        }
        size = s
        seg = LongArray(2 * size) { 0L }
        lazy = LongArray(2 * size) { 0L }
    }
    private fun push(node: Int, l: Int, r: Int) {
        if (lazy[node] != 0L) {
            seg[node] += lazy[node]
            if (l != r) {
                lazy[node * 2] += lazy[node]
                lazy[node * 2 + 1] += lazy[node]
            }
            lazy[node] = 0L
        }
    }
    fun update(ql: Int, qr: Int, delta: Long) {
        update(ql, qr, delta, 1, 0, size - 1)
    }
    private fun update(ql: Int, qr: Int, delta: Long, node: Int, l: Int, r: Int) {
        push(node, l, r)
        if (qr < l || ql > r) return
        if (ql <= l && r <= qr) {
            lazy[node] += delta
            push(node, l, r)
            return
        }
        val mid = (l + r) / 2
        update(ql, qr, delta, node * 2, l, mid)
        update(ql, qr, delta, node * 2 + 1, mid + 1, r)
        seg[node] = maxOf(seg[node * 2], seg[node * 2 + 1])
    }
    fun query(ql: Int, qr: Int): Long {
        return query(ql, qr, 1, 0, size - 1)
    }
    private fun query(ql: Int, qr: Int, node: Int, l: Int, r: Int): Long {
        push(node, l, r)
        if (qr < l || ql > r) return Long.MIN_VALUE
        if (ql <= l && r <= qr) return seg[node]
        val mid = (l + r) / 2
        return maxOf(query(ql, qr, node * 2, l, mid), query(ql, qr, node * 2 + 1, mid + 1, r))
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val parts = reader.readLine().split(" ")
    val N = parts[0].toInt()
    val S = parts[1].toInt()
    val T = parts[2].toInt()
    val times = reader.readLine().split(" ").map { it.toInt() }
    val L = 1000000 + T
    val segTree = SegmentTree(L)
    val output = StringBuilder()
    for (t in times) {
        val left = t
        val right = t + T - 1
        val currentMax = segTree.query(left, right)
        if (currentMax < S) {
            segTree.update(left, right, 1)
            output.append("YES\n")
        } else {
            output.append("NO\n")
        }
    }
    writer.write(output.toString())
    writer.flush()
    writer.close()
    reader.close()
}
