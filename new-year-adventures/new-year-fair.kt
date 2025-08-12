import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Graph(val n: Int) {
    val adj = Array(n + 1) { mutableListOf<Int>() }
    
    fun addEdge(u: Int, v: Int) {
        adj[u].add(v)
        adj[v].add(u)
    }
    
    fun dfs(v: Int, visited: BooleanArray) {
        visited[v] = true
        for (u in adj[v]) {
            if (!visited[u]) dfs(u, visited)
        }
    }
    
    fun countComponents(): Int {
        val visited = BooleanArray(n + 1)
        var components = 0
        for (v in 1..n) {
            if (!visited[v]) {
                dfs(v, visited)
                components++
            }
        }
        return components
    }
}

fun maxRemovableRoutes(n: Int, m: Int, edges: Array<Pair<Int, Int>>): Int {
    val graph = Graph(n)
    edges.forEach { (u, v) -> graph.addEdge(u, v) }
    val components = graph.countComponents()
    val minEdges = n - components
    return m - minEdges
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    val edges = Array(m) {
        val (u, v) = reader.readLine().split(" ").map { it.toInt() }
        Pair(u, v)
    }
    
    val result = maxRemovableRoutes(n, m, edges)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
