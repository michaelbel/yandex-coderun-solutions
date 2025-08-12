import java.io.InputStream

class FastInput(private val stream: InputStream) {
    private val buf = ByteArray(1 shl 16)
    private var cur = 0
    private var len = 0

    @Throws(java.io.IOException::class)
    private fun read(): Int {
        if (cur >= len) {
            cur = 0
            len = stream.read(buf)
            if (len <= 0) return -1
        }
        return buf[cur++].toInt() and 0xFF
    }

    fun nextInt(): Int {
        var c = read()
        while (c <= ' '.code && c != -1) c = read()
        var neg = false
        if (c == '-'.code) {
            neg = true
            c = read()
        }
        var result = 0
        while (c >= '0'.code && c <= '9'.code) {
            result = result * 10 + (c - '0'.code)
            c = read()
        }
        return if (neg) -result else result
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= ' '.code && c != -1) c = read()
        var neg = false
        if (c == '-'.code) {
            neg = true
            c = read()
        }
        var result = 0L
        while (c >= '0'.code && c <= '9'.code) {
            result = result * 10 + (c - '0'.code)
            c = read()
        }
        return if (neg) -result else result
    }
}

fun main() {
    val input = FastInput(System.`in`)
    val n = input.nextInt()
    val totalBoxes = 2 * n - 1

    val m = LongArray(totalBoxes)
    val o = LongArray(totalBoxes)
    var totalM = 0L
    var totalO = 0L

    for (i in 0 until totalBoxes) {
        val mi = input.nextLong()
        val oi = input.nextLong()
        m[i] = mi
        o[i] = oi
        totalM += mi
        totalO += oi
    }

    val twoN = 2L * n
    val angles = DoubleArray(totalBoxes)
    for (i in 0 until totalBoxes) {
        val ai = twoN * m[i] - totalM
        val bi = twoN * o[i] - totalO
        angles[i] = Math.atan2(bi.toDouble(), ai.toDouble())
    }

    val ord = Array(totalBoxes) { it }
    ord.sortWith(Comparator { i, j ->
        angles[i].compareTo(angles[j])
    })

    val sb = StringBuilder()
    sb.ensureCapacity(n * 11)

    var sumM = 0L
    var sumO = 0L
    for (k in 0 until n) {
        sumM += m[ord[k]]
        sumO += o[ord[k]]
    }
    if (sumM * 2 >= totalM && sumO * 2 >= totalO) {
        for (k in 0 until n) {
            sb.append(ord[k] + 1).append(' ')
        }
        print(sb)
        return
    }

    for (i in 1 until totalBoxes) {
        val outIdx = ord[i - 1]
        val inIdx = ord[(i + n - 1) % totalBoxes]
        sumM += m[inIdx] - m[outIdx]
        sumO += o[inIdx] - o[outIdx]
        if (sumM * 2 >= totalM && sumO * 2 >= totalO) {
            for (k in 0 until n) {
                val idx = ord[(i + k) % totalBoxes]
                sb.append(idx + 1).append(' ')
            }
            print(sb)
            return
        }
    }

    for (i in 0 until n) {
        sb.append(i + 1).append(' ')
    }
    print(sb)
}
