import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val parentsInput = reader.readLine().split(" ").map { it.toInt() }
    val adj = List(n) { mutableListOf<Int>() }
    var directorId = -1
    for (i in 0 until n) {
        val managerInputIndex = parentsInput[i]
        if (managerInputIndex == 0) {
            directorId = i
        } else {
            val parentId = managerInputIndex - 1
            adj[parentId].add(i)
        }
    }
    val depths = IntArray(n) { -1 }
    val stack = ArrayDeque<Pair<Int, Int>>()
    if (directorId != -1) {
        depths[directorId] = 0
        stack.push(directorId to 0)
        while (stack.isNotEmpty()) {
            val (currentEmployee, currentDepth) = stack.pop()
            for (child in adj[currentEmployee]) {
                if (depths[child] == -1) {
                    depths[child] = currentDepth + 1
                    stack.push(child to currentDepth + 1)
                }
            }
        }
    }
    writer.write(depths.joinToString(" "))
    writer.newLine()
    reader.close()
    writer.close()
}
