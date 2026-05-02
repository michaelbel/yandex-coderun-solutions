import java.io.BufferedInputStream

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
        return res * sign
    }
}

fun main() {
    val fs = FastScanner()
    val a = IntArray(10)
    for (i in 0 until 10) a[i] = fs.nextInt()

    var bestSum = 0
    var bestDist = 100

    for (mask in 0 until (1 shl 10)) {
        var s = 0
        var m = mask
        var i = 0
        while (i < 10) {
            if ((m and 1) != 0) s += a[i]
            m = m ushr 1
            i++
        }
        val dist = kotlin.math.abs(100 - s)
        if (dist < bestDist || (dist == bestDist && s > bestSum)) {
            bestDist = dist
            bestSum = s
        }
    }

    print(bestSum)
}
