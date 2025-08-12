import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val vertices = mutableSetOf<String>()
    val edges = mutableMapOf<Pair<String, String>, Int>()
    val t = reader.readLine().toInt()
    repeat(t) {
        val word = reader.readLine()
        if (word.length >= 4) {
            var prev = word.substring(0, 3)
            vertices.add(prev)
            for (i in 1..word.length - 3) {
                val current = word.substring(i, i + 3)
                vertices.add(current)
                val edge = Pair(prev, current)
                edges[edge] = edges.getOrDefault(edge, 0) + 1
                prev = current
            }
        }
    }
    writer.write("${vertices.size}\n")
    writer.write("${edges.size}\n")
    edges.forEach { (edgePair, weight) ->
        writer.write("${edgePair.first} ${edgePair.second} $weight\n")
    }
    writer.flush()
    reader.close()
    writer.close()
}
