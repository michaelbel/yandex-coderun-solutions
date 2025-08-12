import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class Edge(val u: Int, val v: Int, val w: Int)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val m = firstLine[1].toInt()
    val edges = Array(m) {
        val parts = reader.readLine().split(" ")
        val u = parts[0].toInt() - 1
        val v = parts[1].toInt() - 1
        val w = parts[2].toInt()
        Edge(u, v, w)
    }
    edges.sortBy { it.w }
    val parent = IntArray(n) { it }
    val rank = IntArray(n)
    val color = IntArray(n)
    fun find(x: Int): Int {
        if (parent[x] != x) {
            val root = find(parent[x])
            color[x] = color[x] xor color[parent[x]]
            parent[x] = root
        }
        return parent[x]
    }
    fun union(x: Int, y: Int, diff: Int): Boolean {
        val rx = find(x)
        val ry = find(y)
        if (rx == ry) {
            return (color[x] xor color[y]) == diff
        }
        if (rank[rx] < rank[ry]) {
            parent[rx] = ry
            color[rx] = color[x] xor color[y] xor diff
        } else {
            parent[ry] = rx
            color[ry] = color[x] xor color[y] xor diff
            if (rank[rx] == rank[ry]) rank[rx]++
        }
        return true
    }
    var answer = 0
    for (edge in edges) {
        if (!union(edge.u, edge.v, 1)) {
            answer = edge.w
            break
        }
    }
    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
