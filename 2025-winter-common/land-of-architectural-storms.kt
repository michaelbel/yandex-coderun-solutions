import java.io.BufferedInputStream
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
        do c = readByte() while (c <= 32 && c >= 0)
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
}

fun main() {
    val fs = FastScanner()
    val n = fs.nextInt()
    val p = IntArray(n + 1)
    for (i in 1..n) p[i] = fs.nextInt()

    val q = IntArray(n + 1)

    if (n % 2 == 0) {
        var i = 1
        while (i <= n) {
            val a = i
            val b = i + 1
            if (p[a] != a && p[b] != b) {
                q[a] = a
                q[b] = b
            } else {
                q[a] = b
                q[b] = a
            }
            i += 2
        }
    } else {
        var i = 1
        while (i <= n - 3) {
            val a = i
            val b = i + 1
            if (p[a] != a && p[b] != b) {
                q[a] = a
                q[b] = b
            } else {
                q[a] = b
                q[b] = a
            }
            i += 2
        }

        val a = n - 2
        val b = n - 1
        val c = n
        val perms = arrayOf(
            intArrayOf(a, b, c),
            intArrayOf(a, c, b),
            intArrayOf(b, a, c),
            intArrayOf(b, c, a),
            intArrayOf(c, a, b)
        )

        var done = false
        for (perm in perms) {
            if (perm[0] != p[a] && perm[1] != p[b] && perm[2] != p[c]) {
                q[a] = perm[0]
                q[b] = perm[1]
                q[c] = perm[2]
                done = true
                break
            }
        }
        if (!done) {
            q[a] = b
            q[b] = c
            q[c] = a
        }
    }

    val out = StringBuilder(n * 2)
    for (i in 1..n) {
        if (i > 1) out.append(' ')
        out.append(q[i])
    }
    print(out.toString())
}
