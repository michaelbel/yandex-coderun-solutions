import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val n = reader.readLine().toInt()
    val firstQueue = ArrayDeque<Int>()
    val secondQueue = ArrayDeque<Int>()

    repeat(n) {
        val command = reader.readLine().split(" ")

        when (command[0]) {
            "+" -> secondQueue.addLast(command[1].toInt())
            "*" -> firstQueue.addLast(command[1].toInt())
            "-" -> writer.write("${firstQueue.pollFirst()}\n")
        }

        while (firstQueue.size < secondQueue.size) {
            firstQueue.addLast(secondQueue.pollFirst())
        }
        while (firstQueue.size > secondQueue.size + 1) {
            secondQueue.addFirst(firstQueue.pollLast())
        }
    }

    reader.close()
    writer.close()
}
