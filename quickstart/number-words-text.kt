import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val uniqueWords = mutableSetOf<String>()
    var line = reader.readLine()
    while (line != null) {
        val words = line.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        uniqueWords.addAll(words)
        line = reader.readLine()
    }
    writer.write("${uniqueWords.size}\n")
    writer.flush()
    writer.close()
    reader.close()
}
