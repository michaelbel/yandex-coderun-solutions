import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

private fun cleanupAfterDestruction(s0: Long, e0: Long): Long {
    var s = s0
    var e = e0
    var rounds = 0L
    while (true) {
        if (e == 0L) return rounds
        if (s == 0L) return -1L
        if (s >= e) return rounds + 1
        if (e >= 2L * s) return -1L
        val e1 = e - s
        val s1 = s - e1
        s = s1
        e = e1
        rounds++
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val x = reader.readLine().trim().toLong()
    val y = reader.readLine().trim().toLong()
    val p = reader.readLine().trim().toLong()

    if (y <= x) {
        writer.write("1\n")
        writer.flush()
        reader.close()
        writer.close()
        return
    }

    var best: Long? = null

    if (x < p) {
        val need = y - x
        if (need in 1..x) {
            val k = x - need
            val eRem = if (p > k) p - k else 0L
            val sAfter = x - min(eRem, x)
            val tail = cleanupAfterDestruction(sAfter, eRem)
            if (tail != -1L) best = 2L + tail
        }
    } else {
        var r = 2L
        var remain = y - x
        val step = x - p

        if (x == p) {
            if (remain in 1..x) {
                val k = x - remain
                val eRem = if (p > k) p - k else 0L
                val sAfter = x - min(eRem, x)
                val tail = cleanupAfterDestruction(sAfter, eRem)
                if (tail != -1L) best = 2L + tail
            }
        } else {
            while (remain > 0) {
                if (remain <= x) {
                    val k = x - remain
                    val eRem = if (p > k) p - k else 0L
                    val sAfter = x - min(eRem, x)
                    val tail = cleanupAfterDestruction(sAfter, eRem)
                    if (tail != -1L) {
                        val total = r + tail
                        best = if (best == null || total < best!!) total else best
                    }
                }
                remain -= step
                r++
            }
        }
    }

    writer.write((best ?: -1L).toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
