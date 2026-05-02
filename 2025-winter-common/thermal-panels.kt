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

    fun nextLong(): Long {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0L
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
    val r = fs.nextLong()
    val b = fs.nextLong()

    var i = 1L
    while (i * i <= b) {
        if (b % i == 0L) {
            val hInner = i
            val wInner = b / i
            val w = wInner + 2
            val h = hInner + 2
            val perimeter = 2 * w + 2 * h - 4
            if (perimeter == r) {
                if (w >= h) {
                    print("$w $h")
                } else {
                    print("$h $w")
                }
                return
            }
        }
        i++
    }
}
