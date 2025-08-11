import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val freq = mutableMapOf<String, Int>()

    while (true) {
        val line = reader.readLine() ?: break
        val words = line.split(Regex("\\s+")).filter { it.isNotEmpty() }
        for (word in words) {
            val countSoFar = freq.getOrElse(word) { 0 }
            writer.write("$countSoFar ")
            freq[word] = countSoFar + 1
        }
    }

    writer.newLine()
    reader.close()
    writer.close()
}
