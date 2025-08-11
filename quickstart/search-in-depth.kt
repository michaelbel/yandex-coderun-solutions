import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (_, m) = reader.readLine().split(" ").map(String::toInt)

    val graph = mutableMapOf<Int, MutableList<Int>>()
    repeat(m) {
        val (u, v) = reader.readLine().split(" ").map(String::toInt)
        graph.computeIfAbsent(u) { mutableListOf() }.add(v)
        graph.computeIfAbsent(v) { mutableListOf() }.add(u)
    }

    val visited = mutableSetOf<Int>()

    fun dfs(vertex: Int) {
        visited.add(vertex)
        for (neighbor in graph[vertex] ?: emptyList()) {
            if (neighbor !in visited) {
                dfs(neighbor)
            }
        }
    }

    dfs(1)

    val result = visited.sorted()
    writer.write("${result.size}\n")
    writer.write(result.joinToString(" "))
    writer.newLine()

    reader.close()
    writer.close()
}
