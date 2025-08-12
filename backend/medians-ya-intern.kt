import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.PriorityQueue

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val x = reader.readLine().split(" ").map { it.toLong() }.toLongArray()
    val smaller = PriorityQueue<Long>{a,b -> b.compareTo(a)}
    val larger = PriorityQueue<Long>()
    var sum = 0L
    for (i in 0 until n) {
        val current = x[i]
        if (smaller.isEmpty() || current < smaller.peek()) smaller.add(current) else larger.add(current)
        if (smaller.size > larger.size + 1) larger.add(smaller.poll()) else if (larger.size > smaller.size) smaller.add(larger.poll())
        sum += smaller.peek()
    }
    writer.write(sum.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
