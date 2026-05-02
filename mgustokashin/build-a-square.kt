import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val n = reader.readLine().toInt()
    val xs = IntArray(n)
    val ys = IntArray(n)
    for (i in 0 until n) {
        val line = reader.readLine()
        val sp = line.indexOf(' ')
        xs[i] = line.substring(0, sp).toInt()
        ys[i] = line.substring(sp + 1).toInt()
    }

    if (n == 1) {
        val x0 = xs[0]
        val y0 = ys[0]
        writer.write("3\n")
        writer.write("${x0 + 1} $y0\n")
        writer.write("$x0 ${y0 + 1}\n")
        writer.write("${x0 + 1} ${y0 + 1}\n")
        writer.flush()
        return
    }

    val OFFSET = 100_000_000
    var cap = 1
    while (cap < n * 4) cap = cap shl 1
    val mask = cap - 1
    val EMPTY = -1L
    val table = LongArray(cap) { EMPTY }

    fun encode(x: Int, y: Int): Long {
        return ((x + OFFSET).toLong() shl 32) or ((y + OFFSET).toLong() and 0xFFFFFFFFL)
    }

    fun containsKey(key: Long): Boolean {
        var idx = ((key xor (key ushr 32)).toInt()) and mask
        while (true) {
            val v = table[idx]
            if (v == EMPTY) return false
            if (v == key) return true
            idx = (idx + 1) and mask
        }
    }

    for (i in 0 until n) {
        val key = encode(xs[i], ys[i])
        var idx = ((key xor (key ushr 32)).toInt()) and mask
        while (table[idx] != EMPTY) {
            idx = (idx + 1) and mask
        }
        table[idx] = key
    }

    var bestMissing = 3
    var ax1 = 0; var ay1 = 0
    var ax2 = 0; var ay2 = 0

    outer@ for (i in 0 until n) {
        val x1 = xs[i]
        val y1 = ys[i]
        for (j in i + 1 until n) {
            if (bestMissing == 0) break@outer

            val x2 = xs[j]
            val y2 = ys[j]
            val dx = x2 - x1
            val dy = y2 - y1

            run {
                var miss = 0
                var tx1 = 0; var ty1 = 0
                var tx2 = 0; var ty2 = 0

                val rx = x1 - dy
                val ry = y1 + dx
                val sx = x2 - dy
                val sy = y2 + dx

                val keyR = encode(rx, ry)
                if (!containsKey(keyR)) {
                    miss++; tx1 = rx; ty1 = ry
                }
                val keyS = encode(sx, sy)
                if (!containsKey(keyS)) {
                    miss++
                    if (miss == 1) { tx1 = sx; ty1 = sy }
                    else { tx2 = sx; ty2 = sy }
                }
                if (miss < bestMissing) {
                    bestMissing = miss
                    if (miss >= 1) { ax1 = tx1; ay1 = ty1 }
                    if (miss == 2) { ax2 = tx2; ay2 = ty2 }
                }
            }

            if (bestMissing > 0) {
                run {
                    var miss = 0
                    var tx1 = 0; var ty1 = 0
                    var tx2 = 0; var ty2 = 0

                    val rx = x1 + dy
                    val ry = y1 - dx
                    val sx = x2 + dy
                    val sy = y2 - dx

                    val keyR = encode(rx, ry)
                    if (!containsKey(keyR)) {
                        miss++; tx1 = rx; ty1 = ry
                    }
                    val keyS = encode(sx, sy)
                    if (!containsKey(keyS)) {
                        miss++
                        if (miss == 1) { tx1 = sx; ty1 = sy }
                        else { tx2 = sx; ty2 = sy }
                    }
                    if (miss < bestMissing) {
                        bestMissing = miss
                        if (miss >= 1) { ax1 = tx1; ay1 = ty1 }
                        if (miss == 2) { ax2 = tx2; ay2 = ty2 }
                    }
                }
            }

            if (bestMissing > 0) {
                val sx = x1 + x2
                val sy = y1 + y2
                if ((sx and 1) == 0 && (sy and 1) == 0) {
                    val mx = sx shr 1
                    val my = sy shr 1
                    val hx = x1 - mx
                    val hy = y1 - my
                    val px = -hy
                    val py = hx
                    val rx = mx + px
                    val ry = my + py
                    val sx2 = mx - px
                    val sy2 = my - py

                    var miss = 0
                    var tx1 = 0; var ty1 = 0
                    var tx2 = 0; var ty2 = 0

                    val keyR = encode(rx, ry)
                    if (!containsKey(keyR)) {
                        miss++; tx1 = rx; ty1 = ry
                    }
                    val keyS = encode(sx2, sy2)
                    if (!containsKey(keyS)) {
                        miss++
                        if (miss == 1) { tx1 = sx2; ty1 = sy2 }
                        else { tx2 = sx2; ty2 = sy2 }
                    }
                    if (miss < bestMissing) {
                        bestMissing = miss
                        if (miss >= 1) { ax1 = tx1; ay1 = ty1 }
                        if (miss == 2) { ax2 = tx2; ay2 = ty2 }
                    }
                }
            }
        }
    }

    writer.write("$bestMissing\n")
    if (bestMissing >= 1) writer.write("$ax1 $ay1\n")
    if (bestMissing == 2) writer.write("$ax2 $ay2\n")
    writer.flush()
}
