import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val points = Array(n) {
        val (x, y) = reader.readLine().split(" ").map { it.toInt() }
        y
    }

    val forward = IntArray(n)
    forward[0] = 0
    for (i in 1 until n) {
        forward[i] = forward[i - 1] + if (points[i] > points[i - 1]) (points[i] - points[i - 1]) else 0
    }

    val rev = IntArray(n)
    rev[0] = 0
    for (i in 1 until n) {
        rev[i] = rev[i - 1] + if (points[i - 1] > points[i]) (points[i - 1] - points[i]) else 0
    }

    val m = reader.readLine().toInt()

    repeat(m) {
        val (s, f) = reader.readLine().split(" ").map { it.toInt() }
        if (s == f) {
            writer.write("0")
            writer.newLine()
        } else if (s < f) {
            writer.write("${forward[f - 1] - forward[s - 1]}")
            writer.newLine()
        } else {
            writer.write("${rev[s - 1] - rev[f - 1]}")
            writer.newLine()
        }
    }

    writer.flush()
    reader.close()
    writer.close()
}