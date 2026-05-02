import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.sqrt

private class FastScanner(private val reader: BufferedReader) {
    private var st: StringTokenizer? = null

    fun nextInt(): Int = nextLong().toInt()

    fun nextLong(): Long {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return 0L
            st = StringTokenizer(line)
        }
        return st!!.nextToken().toLong()
    }
}

private fun pack(x: Int, y: Int): Long {
    return (x.toLong() shl 32) xor (y.toLong() and 0xffffffffL)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val dLong = fs.nextLong()

    val xs = IntArray(n)
    val ys = IntArray(n)
    val set = HashSet<Long>(n * 2)

    for (i in 0 until n) {
        val x = fs.nextInt()
        val y = fs.nextInt()
        xs[i] = x
        ys[i] = y
        set.add(pack(x, y))
    }

    val d = dLong
    val limit = sqrt(d.toDouble()).toInt()
    val maxVec = 4 * limit + 5
    val dxArr = IntArray(maxVec)
    val dyArr = IntArray(maxVec)
    var vCount = 0

    var dx = -limit
    while (dx <= limit) {
        val x2 = dx.toLong() * dx.toLong()
        val rem = d - x2
        if (rem >= 0L) {
            val yAbs = sqrt(rem.toDouble()).toLong()
            if (yAbs * yAbs == rem) {
                if (yAbs == 0L) {
                    dxArr[vCount] = dx
                    dyArr[vCount] = 0
                    vCount++
                } else {
                    dxArr[vCount] = dx
                    dyArr[vCount] = yAbs.toInt()
                    vCount++
                    dxArr[vCount] = dx
                    dyArr[vCount] = (-yAbs).toInt()
                    vCount++
                }
            }
        }
        dx++
    }

    if (vCount == 0) {
        writer.write("0\n")
        writer.flush()
        return
    }

    var totalDirected = 0L
    var i = 0
    while (i < n) {
        val x = xs[i]
        val y = ys[i]
        var j = 0
        while (j < vCount) {
            val nx = x + dxArr[j]
            val ny = y + dyArr[j]
            if (set.contains(pack(nx, ny))) {
                totalDirected++
            }
            j++
        }
        i++
    }

    val result = totalDirected / 2L
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
