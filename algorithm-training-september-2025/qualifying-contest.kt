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

    val n = scanner.nextInt()
    val k = scanner.nextInt()

    val freq = HashMap<Int, Int>(n)
    val topics = IntArray(n)
    for (i in 0 until n) {
        val x = scanner.nextInt()
        topics[i] = x
        freq[x] = (freq[x] ?: 0) + 1
    }

    // Список различных тем (по одной каждой)
    val distinctTopics = ArrayList<Int>()
    val keys = freq.keys.toList()
    for (topic in keys) {
        val f = freq[topic] ?: 0
        if (f > 0) {
            distinctTopics.add(topic)
            freq[topic] = f - 1
        }
    }

    val distinctCount = distinctTopics.size
    val result = ArrayList<Int>(k)

    if (k <= distinctCount) {
        for (i in 0 until k) {
            result.add(distinctTopics[i])
        }
    } else {
        result.addAll(distinctTopics)
        var remaining = k - distinctCount

        outer@ for (topic in keys) {
            var left = freq[topic] ?: 0
            while (left > 0 && remaining > 0) {
                result.add(topic)
                left--
                remaining--
                if (remaining == 0) break@outer
            }
            freq[topic] = left
        }
    }

    val sb = StringBuilder()
    for (i in 0 until k) {
        if (i > 0) sb.append(' ')
        sb.append(result[i])
    }
    writer.write(sb.toString())
    writer.newLine()
    writer.flush()
}
