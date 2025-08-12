import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

data class Point(val x: Int, val y: Int)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(' ').map { it.toInt() }
    val grid = Array(n) { reader.readLine() }
    val adj = mutableMapOf<Point, MutableList<Point>>()
    fun addEdge(p: Point, q: Point) {
        adj.getOrPut(p) { mutableListOf() }.add(q)
        adj.getOrPut(q) { mutableListOf() }.add(p)
    }
    for (i in 0 until n) {
        for (j in 0 until m) {
            when (grid[i][j]) {
                '/' -> {
                    val p1 = Point(j, i + 1)
                    val p2 = Point(j + 1, i)
                    addEdge(p1, p2)
                }
                '\\' -> {
                    val p1 = Point(j, i)
                    val p2 = Point(j + 1, i + 1)
                    addEdge(p1, p2)
                }
                else -> { }
            }
        }
    }
    fun edgeKey(a: Point, b: Point): Pair<Point, Point> =
        if (a.x < b.x || (a.x == b.x && a.y < b.y)) Pair(a, b) else Pair(b, a)
    val visitedEdges = mutableSetOf<Pair<Point, Point>>()
    var totalArea = 0L
    for (u in adj.keys) {
        for (v in adj[u]!!) {
            val keyUV = edgeKey(u, v)
            if (keyUV in visitedEdges) continue
            val cycle = mutableListOf<Point>()
            cycle.add(u)
            var prev = u
            var curr = v
            visitedEdges.add(keyUV)
            cycle.add(curr)
            while (curr != u) {
                val neigh = adj[curr]!!
                val next = if (neigh[0] != prev) neigh[0] else neigh[1]
                val keyCN = edgeKey(curr, next)
                visitedEdges.add(keyCN)
                cycle.add(next)
                prev = curr
                curr = next
            }
            var sum = 0L
            for (i in 0 until cycle.size - 1) {
                val p1 = cycle[i]
                val p2 = cycle[i + 1]
                sum += p1.x.toLong() * p2.y - p2.x.toLong() * p1.y
            }
            totalArea += kotlin.math.abs(sum) / 2
        }
    }
    writer.write(totalArea.toString())
    writer.newLine()
    writer.flush()
    writer.close()
    reader.close()
}
