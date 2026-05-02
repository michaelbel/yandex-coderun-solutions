import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    fun nextInt(): Int = next().toInt()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    val first = scanner.next()
    if (first.isEmpty()) {
        writer.flush()
        return
    }

    val n = first.toInt()
    var sum = 0L
    var minOdd = Int.MAX_VALUE
    var maxEven = Int.MIN_VALUE

    for (i in 0 until n) {
        val value = scanner.nextInt()
        if (i % 2 == 0) {
            sum += value.toLong()
            if (value < minOdd) {
                minOdd = value
            }
        } else {
            sum -= value.toLong()
            if (value > maxEven) {
                maxEven = value
            }
        }
    }

    var bestDiff = maxEven - minOdd
    if (bestDiff < 0) {
        bestDiff = 0
    }

    val result = sum + 2L * bestDiff.toLong()
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
