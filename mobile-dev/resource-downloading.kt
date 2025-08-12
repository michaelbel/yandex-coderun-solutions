import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val graph = mutableMapOf<Int, MutableList<Int>>()
    repeat(n) {
        val (u, v) = reader.readLine().split(" ").map { it.toInt() }
        graph.computeIfAbsent(u) { mutableListOf() }.add(v)
        graph.computeIfAbsent(v) { mutableListOf() }.add(u)
    }
    val warehouseCluster = mutableMapOf<Int, Int>()
    var clusterId = 0
    fun dfs(node: Int) {
        val stack = mutableListOf(node)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (current in warehouseCluster) continue
            warehouseCluster[current] = clusterId
            graph[current]?.forEach { stack.add(it) }
        }
    }
    for (warehouse in graph.keys) {
        if (warehouse !in warehouseCluster) {
            dfs(warehouse)
            clusterId++
        }
    }
    val t = reader.readLine().toInt()
    repeat(t) {
        val (x, k) = reader.readLine().split(" ").map { it.toInt() }
        val suppliers = reader.readLine().split(" ").map { it.toInt() }
        val validSuppliers = suppliers.filter { warehouseCluster[it] == warehouseCluster[x] }
        writer.write("${validSuppliers.size} ${validSuppliers.joinToString(" ")}\n")
    }
    writer.flush()
    reader.close()
    writer.close()
}
