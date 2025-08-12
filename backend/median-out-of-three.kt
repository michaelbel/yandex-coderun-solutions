import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (a, b, c) = reader.readLine().split(" ").map(String::toInt)
    val median = when {
        (a in b..c) || (a in c..b) -> a
        (b in a..c) || (b in c..a) -> b
        else -> c
    }
    writer.write(median.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
