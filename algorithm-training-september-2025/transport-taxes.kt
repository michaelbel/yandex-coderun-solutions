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
    fun nextLong(): Long = next().toLong()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val b = LongArray(n)
    val t = LongArray(n)
    for (i in 0 until n) {
        b[i] = fs.nextLong()
        t[i] = fs.nextLong()
    }

    val m = fs.nextInt()
    val sb = StringBuilder()

    fun rateForPower(q: Long): Long {
        // если мощность больше максимальной указанной в таблице
        if (q > b[n - 1]) return t[n - 1]

        // ищем первый индекс с b[idx] >= q
        var l = 0
        var r = n - 1
        while (l < r) {
            val mid = (l + r) ushr 1
            if (b[mid] >= q) {
                r = mid
            } else {
                l = mid + 1
            }
        }
        // ставка из предыдущей строки (т.к. диапазон (b[i], b[i+1]])
        return t[l - 1]
    }

    repeat(m) {
        val q = fs.nextLong()
        val rate = rateForPower(q)
        val tax = q * rate      // в long влезает: максимум 1e9 * 1e9 = 1e18
        sb.append(tax).append('\n')
    }

    writer.write(sb.toString())
    writer.flush()
}
