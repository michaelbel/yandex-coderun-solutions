import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine() ?: ""
    val n = s.length.toLong()

    val freq = IntArray(26)
    for (ch in s) {
        freq[ch - 'a']++
    }

    val totalPairs = n * (n - 1L) / 2L
    var samePairs = 0L
    for (f in freq) {
        samePairs += f.toLong() * (f - 1L) / 2L
    }

    val result = 1L + (totalPairs - samePairs)
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
