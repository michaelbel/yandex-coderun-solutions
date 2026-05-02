import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private class FastScanner(private val reader: BufferedReader) {
    private val buffer = CharArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun read(): Int {
        if (ptr >= len) {
            len = reader.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].code
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= 32 && c >= 0) {
            c = read()
        }
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = read()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + (c - 48)
            c = read()
        }
        return if (sign == 1) res else -res
    }

    fun nextInt(): Int = nextLong().toInt()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val a = LongArray(n)
    for (i in 0 until n) {
        a[i] = fs.nextLong()
    }

    // Два указателя: Васе слева, Маше справа.
    var i = 0
    var j = n - 1
    var sumV = a[i]
    var sumM = a[j]

    var bestDiff = kotlin.math.abs(sumV - sumM)
    var bestL = 1          // 1-based
    var bestR = n          // 1-based

    // Пока между ними остаётся хотя бы один стол (l < r)
    while (i + 1 < j) {
        if (sumV <= sumM) {
            i++
            sumV += a[i]
        } else {
            j--
            sumM += a[j]
        }

        val diff = kotlin.math.abs(sumV - sumM)
        if (diff < bestDiff) {
            bestDiff = diff
            bestL = i + 1       // 1-based
            bestR = j + 1       // 1-based
        }
    }

    writer.write("$bestDiff $bestL $bestR\n")
    writer.flush()
}
