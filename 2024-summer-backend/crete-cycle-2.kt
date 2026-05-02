import java.io.BufferedInputStream

private const val MOD: Long = 998244353L

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
    val n = fs.nextInt()
    val m = fs.nextInt()

    val head = IntArray(n) { -1 }
    val to = IntArray(2 * m)
    val next = IntArray(2 * m)
    val u = IntArray(m)
    val v = IntArray(m)

    fun addEdge(id: Int, a: Int, b: Int) {
        u[id] = a
        v[id] = b
        val e1 = id shl 1
        to[e1] = b
        next[e1] = head[a]
        head[a] = e1
        val e2 = e1 + 1
        to[e2] = a
        next[e2] = head[b]
        head[b] = e2
    }

    for (i in 0 until m) {
        val a = fs.nextInt() - 1
        val b = fs.nextInt() - 1
        addEdge(i, a, b)
    }

    val tin = IntArray(n) { -1 }
    val low = IntArray(n)
    val parentV = IntArray(n) { -1 }
    val parentE = IntArray(n) { -1 }
    val isBridge = BooleanArray(m)

    val stackV = IntArray(n)
    val stackIt = IntArray(n)
    var timer = 0
    var sp = 0

    for (root in 0 until n) {
        if (tin[root] != -1) continue
        parentV[root] = -1
        parentE[root] = -1
        tin[root] = timer
        low[root] = timer
        timer++

        stackV[sp] = root
        stackIt[sp] = head[root]
        sp++

        while (sp > 0) {
            val vtx = stackV[sp - 1]
            var e = stackIt[sp - 1]

            if (e == -1) {
                sp--
                val p = parentV[vtx]
                if (p != -1) {
                    val lv = low[vtx]
                    if (lv < low[p]) low[p] = lv
                    if (low[vtx] > tin[p]) {
                        val pe = parentE[vtx]
                        isBridge[pe shr 1] = true
                    }
                }
                continue
            }

            stackIt[sp - 1] = next[e]

            val pe = parentE[vtx]
            if (pe != -1 && e == (pe xor 1)) continue

            val w = to[e]
            if (tin[w] == -1) {
                parentV[w] = vtx
                parentE[w] = e
                tin[w] = timer
                low[w] = timer
                timer++

                stackV[sp] = w
                stackIt[sp] = head[w]
                sp++
            } else {
                val tw = tin[w]
                if (tw < low[vtx]) low[vtx] = tw
            }
        }
    }

    val deg = IntArray(n)
    var maxDeg = 0
    for (i in 0 until m) {
        if (!isBridge[i]) continue
        val a = u[i]
        val b = v[i]
        val da = deg[a] + 1
        val db = deg[b] + 1
        deg[a] = da
        deg[b] = db
        if (da > maxDeg) maxDeg = da
        if (db > maxDeg) maxDeg = db
    }

    val inv = LongArray(maxDeg + 1)
    inv[0] = 1L
    if (maxDeg >= 1) inv[1] = 1L
    for (i in 2..maxDeg) {
        val a = inv[i - 1]
        val b = (inv[i - 2] * (i - 1).toLong()) % MOD
        inv[i] = a + b
        if (inv[i] >= MOD) inv[i] -= MOD
    }

    var ans = 1L
    for (i in 0 until n) {
        ans = (ans * inv[deg[i]]) % MOD
    }
    print(ans)
}
