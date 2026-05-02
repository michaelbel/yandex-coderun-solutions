import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.Locale
import kotlin.math.min

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

private fun canScale(
    k: Double,
    n: Int,
    w: Long,
    h: Long,
    a: LongArray,
    b: LongArray
): Boolean {
    if (k <= 0.0) return true

    val limitW = w.toDouble() / k
    val eps = 1e-12

    var lineWidth = 0.0
    var lineHeight = 0L
    var hasLine = false
    var heightSum = 0L

    for (i in 0 until n) {
        val ai = a[i].toDouble()
        val bi = b[i]

        if (!hasLine) {
            // начинаем новую строку
            if (ai - limitW > eps) return false
            lineWidth = ai
            lineHeight = bi
            hasLine = true
        } else {
            if (bi != lineHeight) {
                // обязаны перенести слово на новую строку из-за высоты
                heightSum += lineHeight
                lineHeight = bi
                lineWidth = ai
                if (ai - limitW > eps) return false
            } else {
                // та же высота, пробуем добавить в текущую строку
                if (lineWidth + ai - limitW <= eps) {
                    lineWidth += ai
                } else {
                    // перенос по ширине
                    heightSum += lineHeight
                    lineHeight = bi
                    lineWidth = ai
                    if (ai - limitW > eps) return false
                }
            }
        }
    }

    if (hasLine) {
        heightSum += lineHeight
    }

    // проверяем общую высоту: k * heightSum <= H
    val usedHeight = k * heightSum.toDouble()
    return usedHeight <= h.toDouble() + 1e-9
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    val n = scanner.nextInt()
    val w = scanner.nextLong()
    val h = scanner.nextLong()

    val a = LongArray(n)
    val b = LongArray(n)

    var minA = Long.MAX_VALUE
    var maxB = 0L

    for (i in 0 until n) {
        val ai = scanner.nextLong()
        val bi = scanner.nextLong()
        a[i] = ai
        b[i] = bi
        if (ai < minA) minA = ai
        if (bi > maxB) maxB = bi
    }

    // теоретический верхний предел масштаба
    val k1 = w.toDouble() / minA.toDouble()
    val k2 = h.toDouble() / maxB.toDouble()
    var hi = min(k1, k2)
    if (hi <= 0.0) hi = 1.0

    var lo = 0.0

    // бинпоиск по вещественному масштабу
    for (iter in 0 until 70) {
        val mid = (lo + hi) / 2.0
        if (canScale(mid, n, w, h, a, b)) {
            lo = mid
        } else {
            hi = mid
        }
    }

    val result = lo
    writer.write(String.format(Locale.US, "%.10f", result))
    writer.newLine()
    writer.flush()
}
