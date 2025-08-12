import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun rleTest(code: String): Int =
    code.split(*('A'..'Z').toList().toCharArray())
        .sumOf { if (it.isEmpty()) 1 else it.toInt() } - 1

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val code = reader.readLine()
    val res = rleTest(code)
    writer.write(res.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
