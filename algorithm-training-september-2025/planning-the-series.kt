import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var st: StringTokenizer? = null

    fun nextLong(): Long {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return 0L
            st = StringTokenizer(line)
        }
        return st!!.nextToken().toLong()
    }

    fun nextInt(): Int = nextLong().toInt()
}

private data class Season(val s: Long, val a: Long)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val seasons = Array(n) { Season(0L, 0L) }

    val sArr = LongArray(n)
    for (i in 0 until n) {
        sArr[i] = fs.nextLong()
    }
    val aArr = LongArray(n)
    for (i in 0 until n) {
        aArr[i] = fs.nextLong()
    }

    for (i in 0 until n) {
        seasons[i] = Season(sArr[i], aArr[i])
    }

    seasons.sortBy { it.s }

    var totalA = 0L
    for (i in 0 until n) {
        totalA += seasons[i].a
    }

    val half = (totalA + 1) / 2
    var pref = 0L
    var e = seasons[0].s
    for (i in 0 until n) {
        pref += seasons[i].a
        if (pref >= half) {
            e = seasons[i].s
            break
        }
    }

    var cost = 0L
    for (i in 0 until n) {
        val diff = kotlin.math.abs(e - seasons[i].s)
        cost += diff * seasons[i].a
    }

    writer.write("$e $cost\n")
    writer.flush()
}
