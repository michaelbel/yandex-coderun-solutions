import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (a, b, c) = reader.readLine().split(" ").map { it.toLong() }
    val scores = listOf(a, b, c).sorted()
    val result = scores[1]
    writer.write(result.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
