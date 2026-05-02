import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val input = reader.readLine().split(" ").map { it.toLong() }
    val n = input[0].toInt()
    val heights = input.subList(1, input.size)

    val stack = Stack<Int>()
    var maxArea = 0L

    for (i in 0..n) {
        val currentHeight = if (i < n) heights[i] else 0L

        while (stack.isNotEmpty() && heights[stack.peek()] > currentHeight) {
            val height = heights[stack.pop()]
            val width = if (stack.isEmpty()) i else i - stack.peek() - 1
            maxArea = maxOf(maxArea, height * width)
        }

        stack.push(i)
    }

    writer.write("$maxArea\n")
    reader.close()
    writer.close()
}
