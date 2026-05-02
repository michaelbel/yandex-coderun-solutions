import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private class FastScanner(private val reader: BufferedReader) {
    private val buf = CharArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readChar(): Int {
        if (ptr >= len) {
            len = reader.read(buf)
            ptr = 0
            if (len <= 0) return -1
        }
        return buf[ptr++].code
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

private data class Interval(val start: Double, val end: Double)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val m = fs.nextInt()
    val x = fs.nextLong().toDouble()

    val rawIntervals = ArrayList<Interval>(n)

    repeat(n) {
        val a0 = fs.nextLong().toDouble()
        val b0 = fs.nextLong().toDouble()
        val vAbs = fs.nextLong().toDouble()

        val left0: Double
        val right0: Double
        val s: Double

        if (a0 < b0) {
            left0 = a0
            right0 = b0
            s = vAbs           // едет вправо
        } else {
            left0 = b0
            right0 = a0
            s = -vAbs          // едет влево
        }

        // времена, когда концы поезда проходят через x
        val t1 = (x - left0) / s
        val t2 = (x - right0) / s
        var start = kotlin.math.min(t1, t2)
        val end = kotlin.math.max(t1, t2)

        // нас интересуют только t >= 0, интервал [start, end)
        if (end <= 0.0) return@repeat
        if (start < 0.0) start = 0.0

        if (start < end) {        // ненулевой интервал занятости
            rawIntervals.add(Interval(start, end))
        }
    }

    if (rawIntervals.isEmpty()) {
        // Переезд никогда не занят — машины проходят в момент приезда
        val sb = StringBuilder()
        repeat(m) {
            val t = fs.nextLong().toDouble()
            sb.append(t.toString()).append('\n')
        }
        writer.write(sb.toString())
        writer.flush()
        return
    }

    // сортируем и сливаем интервалы [start, end)
    rawIntervals.sortBy { it.start }
    val mergedStarts = ArrayList<Double>()
    val mergedEnds = ArrayList<Double>()

    val epsMerge = 1e-12

    var curStart = rawIntervals[0].start
    var curEnd = rawIntervals[0].end
    for (i in 1 until rawIntervals.size) {
        val iv = rawIntervals[i]
        if (iv.start <= curEnd + epsMerge) {
            if (iv.end > curEnd) curEnd = iv.end
        } else {
            mergedStarts.add(curStart)
            mergedEnds.add(curEnd)
            curStart = iv.start
            curEnd = iv.end
        }
    }
    mergedStarts.add(curStart)
    mergedEnds.add(curEnd)

    val k = mergedStarts.size
    val startArr = DoubleArray(k)
    val endArr = DoubleArray(k)
    for (i in 0 until k) {
        startArr[i] = mergedStarts[i]
        endArr[i] = mergedEnds[i]
    }

    val epsInside = 1e-9
    val sb = StringBuilder()

    repeat(m) {
        val t = fs.nextLong().toDouble()

        // ищем первый интервал с start > t
        var l = 0
        var r = k
        while (l < r) {
            val mid = (l + r) ushr 1
            if (startArr[mid] > t) {
                r = mid
            } else {
                l = mid + 1
            }
        }
        val idx = l

        val ans = if (idx == 0) {
            // до первого интервала — свободно
            t
        } else {
            val prev = idx - 1
            // проверяем, попали ли внутрь [start, end)
            if (t + epsInside < endArr[prev]) {
                endArr[prev]
            } else {
                t
            }
        }

        sb.append(ans.toString()).append('\n')
    }

    writer.write(sb.toString())
    writer.flush()
}
