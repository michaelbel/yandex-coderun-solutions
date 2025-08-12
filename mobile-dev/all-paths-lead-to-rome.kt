import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun allPathsLeadToRome(verticesCount: Int, edges: List<Pair<Int, Int>>): Int? {
    val vertices = mutableSetOf<Int>().also { it.addAll(List(verticesCount) { i -> i }) }
    edges.forEach { edge ->
        if (edge.first != edge.second) {
            vertices.remove(edge.first)
        }
    }
    if (vertices.size != 1) {
        return null
    }
    val candidate = vertices.first()
    val to = mutableSetOf<Int>()
    edges.forEach { edge ->
        if (edge.second == candidate && edge.first != candidate) {
            to.add(edge.first)
        }
    }
    return if (to.size == verticesCount - 1) candidate else null
}

private fun BufferedWriter.println(s: Any = "") {
    write(s.toString())
    newLine()
}

private fun <T> List<T>.toPair(): Pair<T, T> {
    require(size == 2) { "Incorrect size" }
    return Pair(first(), last())
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (verticesCount, edgesCount) = reader.readLine().split(" ").map { it.toInt() }
    val edges = List(edgesCount) {
        reader.readLine().split(" ").map { it.toInt() - 1 }.toPair()
    }
    val res = allPathsLeadToRome(verticesCount, edges)
    writer.println(res?.plus(1) ?: -1)
    reader.close()
    writer.close()
}
