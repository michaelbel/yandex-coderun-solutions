import java.io.BufferedInputStream
import java.util.Arrays
import java.lang.StringBuilder

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    fun nextInt(): Int {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sign = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return if (sign == 1) res else -res
    }

    fun next(): String {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        val sb = StringBuilder()
        var ch = c
        while (ch > 32 && ch >= 0) {
            sb.append(ch.toChar())
            ch = readByte()
        }
        return sb.toString()
    }
}

private fun upperBound(a: LongArray, x: Long): Int {
    var l = 0
    var r = a.size
    while (l < r) {
        val m = (l + r) ushr 1
        if (a[m] <= x) l = m + 1 else r = m
    }
    return l
}

private fun sumAbs(sorted: LongArray, pref: LongArray, x: Long): Long {
    val n = sorted.size
    val k = upperBound(sorted, x)
    val leftSum = pref[k]
    val rightSum = pref[n] - leftSum
    val left = x * k.toLong() - leftSum
    val right = rightSum - x * (n - k).toLong()
    return left + right
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    val m = fs.nextInt()

    val xs = LongArray(n)
    val ys = LongArray(n)
    for (i in 0 until n) {
        xs[i] = fs.nextInt().toLong()
        ys[i] = fs.nextInt().toLong()
    }

    Arrays.sort(xs)
    Arrays.sort(ys)

    val px = LongArray(n + 1)
    val py = LongArray(n + 1)
    for (i in 0 until n) {
        px[i + 1] = px[i] + xs[i]
        py[i + 1] = py[i] + ys[i]
    }

    val cmd = fs.next()
    var cx = 0L
    var cy = 0L

    val out = StringBuilder(cmd.length * 12)
    for (i in cmd.indices) {
        when (cmd[i]) {
            'N' -> cy++
            'S' -> cy--
            'E' -> cx++
            'W' -> cx--
        }
        val ans = sumAbs(xs, px, cx) + sumAbs(ys, py, cy)
        out.append(ans).append('\n')
    }

    print(out.toString())
}
