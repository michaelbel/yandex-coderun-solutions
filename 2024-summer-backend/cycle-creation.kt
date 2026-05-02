import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

data class Frame(val node: Int, val parent: Int, val parentEdge: Int, var nextIndex: Int)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))
    val (n, m) = reader.readLine()!!.split(' ').map(String::toInt)

    val adj = Array(n) { mutableListOf<Pair<Int, Int>>() }
    val u = IntArray(m)
    val v = IntArray(m)
    repeat(m) { i ->
        val (ui, vi) = reader.readLine()!!.split(' ').map(String::toInt)
        u[i] = ui - 1
        v[i] = vi - 1
        adj[u[i]].add(Pair(v[i], i))
        adj[v[i]].add(Pair(u[i], i))
    }

    val visited = BooleanArray(n)
    val depth = IntArray(n)
    val low = IntArray(n)
    var timer = 0
    val isBridge = BooleanArray(m)
    val stack = mutableListOf<Frame>()
    for (start in 0 until n) {
        if (!visited[start]) {
            stack.add(Frame(start, -1, -1, 0))
            while (stack.isNotEmpty()) {
                val frame = stack.last()
                val x = frame.node
                if (frame.nextIndex == 0) {
                    visited[x] = true
                    depth[x] = timer
                    low[x] = timer
                    timer++
                }
                if (frame.nextIndex < adj[x].size) {
                    val (y, eid) = adj[x][frame.nextIndex++]
                    if (eid == frame.parentEdge) continue
                    if (visited[y]) {
                        if (depth[y] < low[x]) low[x] = depth[y]
                    } else {
                        stack.add(Frame(y, x, eid, 0))
                    }
                } else {
                    stack.removeAt(stack.size - 1)
                    if (frame.parent != -1) {
                        if (low[x] < low[frame.parent]) low[frame.parent] = low[x]
                        if (low[x] > depth[frame.parent]) {
                            isBridge[frame.parentEdge] = true
                        }
                    }
                }
            }
        }
    }

    val parent = IntArray(n) { it }
    val compSize = IntArray(n) { 1 }
    fun find(a: Int): Int {
        var x = a
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]
            x = parent[x]
        }
        return x
    }
    fun union(a: Int, b: Int) {
        var ra = find(a)
        var rb = find(b)
        if (ra == rb) return
        if (compSize[ra] < compSize[rb]) {
            val t = ra; ra = rb; rb = t
        }
        parent[rb] = ra
        compSize[ra] += compSize[rb]
    }
    for (i in 0 until m) {
        if (isBridge[i]) {
            union(u[i], v[i])
        }
    }

    val edgeCount = LongArray(n)
    for (i in 0 until m) {
        val ru = find(u[i])
        val rv = find(v[i])
        if (ru == rv) {
            edgeCount[ru]++
        }
    }

    var answer = 0L
    val seen = BooleanArray(n)
    for (i in 0 until n) {
        val r = find(i)
        if (!seen[r]) {
            seen[r] = true
            val sz = compSize[r].toLong()
            answer += sz * (sz - 1) / 2 - edgeCount[r]
        }
    }

    writer.write("$answer\n")
    writer.flush()
}
