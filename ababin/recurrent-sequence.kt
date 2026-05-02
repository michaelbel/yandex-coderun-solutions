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
        do {
            c = readByte()
            if (c == -1) return Int.MIN_VALUE
        } while (c <= 32)
        var sgn = 1
        var res = 0
        var ch = c
        if (ch == '-'.code) {
            sgn = -1
            ch = readByte()
        }
        while (ch > 32 && ch != -1) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return res * sgn
    }
}

private fun powMod(a0: Int, e0: Long, mod: Int): Int {
    var a = a0.toLong() % mod
    var e = e0
    var r = 1L
    while (e > 0) {
        if ((e and 1L) != 0L) r = (r * a) % mod
        a = (a * a) % mod
        e = e shr 1
    }
    return r.toInt()
}

private fun factorize(n0: Int): IntArray {
    var n = n0
    val tmp = IntArray(64)
    var cnt = 0
    var d = 2
    while (d.toLong() * d.toLong() <= n.toLong()) {
        if (n % d == 0) {
            tmp[cnt++] = d
            while (n % d == 0) n /= d
        }
        d++
    }
    if (n > 1) tmp[cnt++] = n
    return tmp.copyOf(cnt)
}

private fun orderInFpStar(x: Int, p: Int): Int {
    if (p == 2) return 1
    if (x % p == 1) return 1
    val n = p - 1
    val primes = factorize(n)
    var ord = n
    for (q in primes) {
        while (ord % q == 0) {
            val cand = ord / q
            if (powMod(x, cand.toLong(), p) == 1) ord = cand else break
        }
    }
    return ord
}

fun main() {
    val fs = FastScanner()
    val p = fs.nextInt()
    val a = fs.nextInt()
    val b = fs.nextInt()
    val q = fs.nextInt()

    val inv = IntArray(p)
    if (p > 1) {
        inv[1] = 1
        for (i in 2 until p) {
            val pi = p / i
            val pm = p % i
            inv[i] = ((p - pi).toLong() * inv[pm].toLong() % p).toInt()
        }
    }

    val inf = p
    val idx = IntArray(p + 1) { -1 }
    val fiArr = IntArray(p + 2)
    val fjArr = IntArray(p + 2)

    var fi = 0
    var fj = 1
    var z = inf
    var i = 0

    while (true) {
        fiArr[i] = fi
        fjArr[i] = fj
        idx[z] = i

        val fk = fj
        val fl = ((a.toLong() * fj.toLong() + b.toLong() * fi.toLong()) % p).toInt()

        val zNext = if (fk == 0) inf else ((fl.toLong() * inv[fk].toLong()) % p).toInt()

        if (zNext == inf && i + 1 > 0) {
            val lr = i + 1
            val c = fl

            val ordC = orderInFpStar((c % p + p) % p, p)
            val logC = IntArray(p) { -1 }
            var cur = 1
            for (t in 0 until ordC) {
                logC[cur] = t
                cur = ((cur.toLong() * c.toLong()) % p).toInt()
            }

            val lrL = lr.toLong()
            val fullPeriod = lrL * ordC.toLong()

            val out = StringBuilder(q * 3)
            repeat(q) {
                val x = fs.nextInt()
                val y = fs.nextInt()

                if (x == 0) {
                    if (y == 0) {
                        out.append("-1\n")
                    } else {
                        val t = logC[y]
                        if (t < 0) {
                            out.append("-1\n")
                        } else {
                            var ans = lrL * t.toLong()
                            if (ans == 0L) ans = fullPeriod
                            out.append(ans).append('\n')
                        }
                    }
                } else {
                    val ratio = ((y.toLong() * inv[x].toLong()) % p).toInt()
                    val r = idx[ratio]
                    if (r < 0 || r == 0) {
                        out.append("-1\n")
                    } else {
                        val fr = fiArr[r]
                        val target = ((x.toLong() * inv[fr].toLong()) % p).toInt()
                        val t = logC[target]
                        if (t < 0) {
                            out.append("-1\n")
                        } else {
                            val ans = r.toLong() + lrL * t.toLong()
                            out.append(ans).append('\n')
                        }
                    }
                }
            }

            print(out.toString())
            return
        }

        fi = fk
        fj = fl
        z = zNext
        i++
    }
}
