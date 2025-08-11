import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.LinkedList
import java.util.Queue

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine()?.toIntOrNull()
    if (n == null || n <= 0) {
        writer.write("-1")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }

    val adjacencyMatrix = Array(n) {
        val line = reader.readLine()?.trim() ?: ""
        if (line.isEmpty()) {
            writer.write("-1")
            writer.newLine()
            reader.close()
            writer.close()
            return
        }
        line.split(" ").map(String::toInt).toIntArray()
    }

    val input = reader.readLine()?.split(" ") ?: emptyList()
    if (input.size < 2) {
        writer.write("-1")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }
    val (start, end) = input.map(String::toInt)

    val visited = BooleanArray(n) { false }
    val distances = IntArray(n) { -1 }
    val queue: Queue<Int> = LinkedList()

    queue.add(start - 1)
    visited[start - 1] = true
    distances[start - 1] = 0

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        for (neighbor in 0 until n) {
            if (adjacencyMatrix[current][neighbor] == 1 && !visited[neighbor]) {
                visited[neighbor] = true
                distances[neighbor] = distances[current] + 1
                queue.add(neighbor)
            }
        }
    }

    writer.write("${distances[end - 1]}")
    writer.newLine()

    reader.close()
    writer.close()
}