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

    fun nextLong(): Long = next().toLong()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    var n = scanner.nextLong()
    var k = scanner.nextLong()

    if (k == 0L) {
        writer.write(n.toString())
        writer.newLine()
        writer.flush()
        return
    }

    val d0 = (n % 10).toInt()

    if (d0 == 0) {
        writer.write(n.toString())
        writer.newLine()
        writer.flush()
        return
    }

    if (d0 == 5) {
        // Один шаг, дальше последняя цифра 0 и число не меняется
        n += 5L
        writer.write(n.toString())
        writer.newLine()
        writer.flush()
        return
    }

    // Последняя цифра в {1,2,3,4,6,7,8,9}
    // Первый шаг делаем явно
    n += d0.toLong()
    k--

    if (k > 0L) {
        val fullBlocks = k / 4L      // каждый блок из 4 шагов добавляет 20
        n += fullBlocks * 20L
        var rest = (k % 4L).toInt()  // оставшиеся шаги (не больше 3)
        while (rest > 0) {
            val d = (n % 10).toInt()
            n += d.toLong()
            rest--
        }
    }

    writer.write(n.toString())
    writer.newLine()
    writer.flush()
}
