import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val lines = mutableListOf<String>()
    while (true) {
        val line = reader.readLine() ?: break
        lines.add(line)
    }
    val result = Array(lines.size) { "" }
    for (line in lines) {
        val indexMatch = Regex("\\d+").find(line)
        if (indexMatch != null) {
            val index = indexMatch.value.toInt() - 1
            val cleanLine = line.replace(indexMatch.value, "")
            result[index] = cleanLine
        }
    }
    for (line in result) {
        writer.write(line)
        writer.newLine()
    }
    reader.close()
    writer.close()
}
