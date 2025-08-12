import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    val graph = Array(n + 1) { BooleanArray(n + 1) }
    repeat(m) {
        val (a, b) = reader.readLine().split(" ").map { it.toInt() }
        graph[a][b] = true
        graph[b][a] = true
    }
    val color = IntArray(n + 1)
    val queue = ArrayDeque<Int>()
    fun opp(c: Int) = if (c == 1) 2 else 1
    for (u in 1..n) {
        if (color[u] == 0) {
            color[u] = 1
            queue.addLast(u)
            while (queue.isNotEmpty()) {
                val cur = queue.removeFirst()
                for (v in 1..n) {
                    if (v == cur) continue
                    if (!graph[cur][v]) {
                        if (color[v] == 0) {
                            color[v] = opp(color[cur])
                            queue.addLast(v)
                        } else if (color[v] == color[cur]) {
                            writer.write("-1")
                            writer.newLine()
                            writer.flush()
                            reader.close()
                            writer.close()
                            return
                        }
                    }
                }
            }
        }
    }
    var count1 = 0
    var count2 = 0
    for (i in 1..n) {
        if (color[i] == 1) count1++ else count2++
    }
    if (count1 == 0 || count2 == 0) {
        for (i in 1..n) {
            var isolated = true
            for (j in 1..n) {
                if (i != j && !graph[i][j]) {
                    isolated = false
                    break
                }
            }
            if (isolated) {
                color[i] = opp(color[i])
                break
            }
        }
    }
    val group1 = mutableListOf<Int>()
    val group2 = mutableListOf<Int>()
    for (i in 1..n) {
        if (color[i] == 1) group1.add(i) else group2.add(i)
    }
    if (group1.isEmpty() || group2.isEmpty()) {
        writer.write("-1")
        writer.newLine()
    } else {
        writer.write("${group1.size}")
        writer.newLine()
        writer.write(group1.joinToString(" "))
        writer.newLine()
        writer.write(group2.joinToString(" "))
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
