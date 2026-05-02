import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private class FastScanner(private val reader: BufferedReader) {
    private val buffer = CharArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readChar(): Int {
        if (ptr >= len) {
            len = reader.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].code
    }

    fun nextLong(): Long {
        var c = readChar()
        while (c <= 32 && c >= 0) {
            c = readChar()
        }
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = readChar()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + (c - 48)
            c = readChar()
        }
        return if (sign == 1) res else -res
    }

    fun nextInt(): Int = nextLong().toInt()
}

private data class Segment(val w: Long, val a: Long)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val m = fs.nextInt()
    var k = fs.nextLong()

    val a = LongArray(n)
    for (i in 0 until n) {
        a[i] = fs.nextLong()
    }

    // difference array to count how many routes go through each segment
    val diff = LongArray(n + 1)
    for (i in 0 until m) {
        val l = fs.nextInt() - 1
        val r = fs.nextInt() - 1
        diff[l] += 1L
        if (r + 1 < n) {
            diff[r + 1] -= 1L
        }
    }

    val w = LongArray(n)
    var cur = 0L
    for (i in 0 until n) {
        cur += diff[i]
        w[i] = cur
    }

    // начальный суммарный дискомфорт
    var initialTotal = 0L
    for (i in 0 until n) {
        if (w[i] != 0L && a[i] != 0L) {
            initialTotal += a[i] * w[i]
        }
    }

    if (k <= 0L) {
        writer.write(initialTotal.toString())
        writer.newLine()
        writer.flush()
        return
    }

    // собираем только сегменты, где есть смысл что-то чинить
    val segments = ArrayList<Segment>(n)
    for (i in 0 until n) {
        if (w[i] > 0L && a[i] > 0L) {
            segments.add(Segment(w[i], a[i]))
        }
    }

    if (segments.isEmpty()) {
        writer.write(initialTotal.toString())
        writer.newLine()
        writer.flush()
        return
    }

    // чиним выбоины там, где каждая даёт максимальный выигрыш: сортировка по w убыванию
    segments.sortByDescending { it.w }

    var remainingK = k
    var totalReduction = 0L

    for (seg in segments) {
        if (remainingK == 0L) break
        val canFix = if (seg.a <= remainingK) seg.a else remainingK
        if (canFix <= 0L) continue
        totalReduction += seg.w * canFix
        remainingK -= canFix
    }

    val result = initialTotal - totalReduction
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
