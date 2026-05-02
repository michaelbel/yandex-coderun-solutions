import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.abs

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
    fun nextLong(): Long = next().toLong()
}

private data class Item(val value: Long, val index: Int)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val pLong = fs.nextLong()
    val pDouble = pLong.toDouble()

    val items = Array(n) { idx ->
        val v = fs.nextLong()
        Item(v, idx + 1) // индексы 1-based
    }

    // сортируем по значениям
    items.sortWith(compareBy<Item> { it.value })

    val values = LongArray(n)
    val idx = IntArray(n)
    for (i in 0 until n) {
        values[i] = items[i].value
        idx[i] = items[i].index
    }

    var bestI = 1
    var bestJ = 2
    var bestDiff = Double.MAX_VALUE

    // бинарный поиск по массиву values
    fun lowerBound(target: Double): Int {
        var l = 0
        var r = n
        while (l < r) {
            val mid = (l + r) ushr 1
            if (values[mid].toDouble() < target) {
                l = mid + 1
            } else {
                r = mid
            }
        }
        return l
    }

    for (d in 0 until n) {
        val denVal = values[d].toDouble()
        val target = pDouble * denVal

        val pos = lowerBound(target)

        // проверяем несколько ближайших по значению кандидатов
        for (k in pos - 2..pos + 2) {
            if (k < 0 || k >= n) continue
            if (k == d) continue  // один и тот же индекс в отсортированном массиве,
                                  // но всё равно проверим по оригинальному на всякий случай
            if (idx[k] == idx[d]) continue

            val ratio = values[k].toDouble() / denVal
            val diff = abs(ratio - pDouble)
            if (diff < bestDiff) {
                bestDiff = diff
                bestI = idx[k]     // числитель
                bestJ = idx[d]     // знаменатель
            }
        }
    }

    writer.write("$bestI $bestJ")
    writer.newLine()
    writer.flush()
}
