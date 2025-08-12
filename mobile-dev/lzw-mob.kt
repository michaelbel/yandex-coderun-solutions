import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun compressString(s: String): List<String> {
    val blocks = mutableListOf<String>()
    val seen = mutableSetOf<String>()
    var i = 0
    while (i < s.length) {
        var prefix = s[i].toString()
        while (i + prefix.length < s.length && prefix in seen) {
            prefix += s[i + prefix.length]
        }
        blocks.add(prefix)
        seen.add(prefix)
        i += prefix.length
    }
    return blocks
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine()
    val result = compressString(s)
    writer.write(result.joinToString(" "))
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
