import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class DSU(n: Int) {
    private val parent = IntArray(n + 1) { it }
    private val rank = IntArray(n + 1)
    var components = n
    fun find(x: Int): Int {
        if (parent[x] != x) parent[x] = find(parent[x])
        return parent[x]
    }
    fun union(x: Int, y: Int) {
        val rx = find(x)
        val ry = find(y)
        if (rx != ry) {
            if (rank[rx] < rank[ry]) parent[rx] = ry
            else if (rank[rx] > rank[ry]) parent[ry] = rx
            else {
                parent[ry] = rx
                rank[rx]++
            }
            components--
        }
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    val edges = Array(m) { IntArray(2) }
    for (i in 0 until m) {
        val (x, y) = reader.readLine().split(" ").map { it.toInt() }
        edges[i][0] = x
        edges[i][1] = y
    }
    val q = reader.readLine().toInt()
    val cuts = reader.readLine().split(" ").map { it.toInt() - 1 }
    val toCut = BooleanArray(m)
    for (cut in cuts) toCut[cut] = true
    val dsu = DSU(n)
    for (i in 0 until m) {
        if (!toCut[i]) dsu.union(edges[i][0], edges[i][1])
    }
    val result = IntArray(q)
    for (i in q - 1 downTo 0) {
        result[i] = dsu.components
        val edge = edges[cuts[i]]
        dsu.union(edge[0], edge[1])
    }
    writer.write(result.joinToString(" "))
    writer.newLine()
    reader.close()
    writer.close()
}
