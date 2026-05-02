import java.io.BufferedInputStream
import java.lang.StringBuilder

private const val MAX = 700000

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
}

private fun dfsDistinctProducts(start: Int, remaining: Int, prod: Int, mark: BooleanArray) {
    if (remaining == 1) {
        val maxF = MAX / prod
        var f = start
        while (f <= maxF) {
            mark[prod * f] = true
            f++
        }
        return
    }

    val maxF = MAX / prod
    var f = start
    while (f <= maxF) {
        val newProd = prod * f
        val limit = (MAX / newProd).toLong()

        var minNext = 1L
        var t = f + 1
        var cnt = remaining - 1
        var ok = true
        while (cnt > 0) {
            val tt = t.toLong()
            if (tt > limit / minNext) {
                ok = false
                break
            }
            minNext *= tt
            t++
            cnt--
        }
        if (!ok) break

        dfsDistinctProducts(f + 1, remaining - 1, newProd, mark)
        f++
    }
}

fun main() {
    val fs = FastScanner()
    val q = fs.nextInt()
    val ks = IntArray(q)
    val ls = IntArray(q)
    val rs = IntArray(q)
    for (i in 0 until q) {
        ks[i] = fs.nextInt()
        ls[i] = fs.nextInt()
        rs[i] = fs.nextInt()
    }

    val pref = Array(9) { IntArray(MAX + 1) }

    for (i in 1..MAX) {
        pref[1][i] = pref[1][i - 1] + if (i >= 2) 1 else 0
    }

    for (k in 2..8) {
        val mark = BooleanArray(MAX + 1)
        dfsDistinctProducts(2, k, 1, mark)
        val pk = pref[k]
        var i = 1
        while (i <= MAX) {
            pk[i] = pk[i - 1] + if (mark[i]) 1 else 0
            i++
        }
    }

    val out = StringBuilder(q * 4)
    for (i in 0 until q) {
        val k = ks[i]
        val l = ls[i]
        val r = rs[i]
        val ans = if (k in 1..8) pref[k][r] - pref[k][l - 1] else 0
        out.append(ans).append('\n')
    }
    print(out.toString())
}
