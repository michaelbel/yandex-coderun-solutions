import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun dfs(start: Int, graph: List<List<Pair<Int, Int>>>, distances: LongArray, visited: BooleanArray) {
    visited[start] = true
    for ((next, weight) in graph[start]) {
        if (!visited[next]) {
            distances[next] = distances[start] + weight
            dfs(next, graph, distances, visited)
        }
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()

    val graph = List(n + 1) { mutableListOf<Pair<Int, Int>>() }
    repeat(n - 1) {
        val (a, b, c) = reader.readLine().split(" ").map { it.toInt() }
        graph[a].add(b to c)
        graph[b].add(a to c)
    }

    var minRadius = Long.MAX_VALUE

    for (u in 1..n) {
        for ((v, weight) in graph[u]) {
            if (u < v) {
                val distU = LongArray(n + 1) { Long.MAX_VALUE }
                distU[u] = 0
                dfs(u, graph, distU, BooleanArray(n + 1))

                val distV = LongArray(n + 1) { Long.MAX_VALUE }
                distV[v] = 0
                dfs(v, graph, distV, BooleanArray(n + 1))

                var maxMinDist = 0L
                for (w in 1..n) {
                    val minDist = minOf(distU[w], distV[w])
                    maxMinDist = maxOf(maxMinDist, minDist)
                }
                minRadius = minOf(minRadius, maxMinDist)
            }
        }
    }

    writer.write(minRadius.toString())
    writer.newLine()

    reader.close()
    writer.close()
}

