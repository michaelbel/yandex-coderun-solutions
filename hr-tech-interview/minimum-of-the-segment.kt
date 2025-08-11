import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val input = reader.readText().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    var index = 0
    val n = input[index++].toInt()
    val k = input[index++].toInt()

    val arr = IntArray(n) { input[index++].toInt() }
    val deque = ArrayDeque<Int>(n)
    val sb = StringBuilder()

    for (i in 0 until n) {
        while (deque.isNotEmpty() && deque.first() <= i - k) {
            deque.removeFirst()
        }

        while (deque.isNotEmpty() && arr[deque.last()] >= arr[i]) {
            deque.removeLast()
        }

        deque.addLast(i)

        if (i >= k - 1) {
            sb.append(arr[deque.first()]).append("\n")
        }
    }

    writer.write(sb.toString())
    writer.flush()

    reader.close()
    writer.close()
}