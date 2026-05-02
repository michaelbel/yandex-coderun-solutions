import java.io.DataInputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter

private const val MOD = 1_000_000_007
private const val MAXX = 1_000_000

class FastReader {
    private val din = DataInputStream(System.`in`)
    private val buf = ByteArray(1 shl 16)
    private var ptr = 0
    private var buflen = 0

    private fun read(): Int {
        if (buflen == -1) return -1
        if (ptr >= buflen) {
            buflen = din.read(buf)
            ptr = 0
            if (buflen <= 0) return -1
        }
        return buf[ptr++].toInt()
    }

    fun nextInt(): Int {
        var c = read()
        while (c != -1 && c <= ' '.code) c = read()
        if (c == -1) return -1
        var neg = false
        if (c == '-'.code) {
            neg = true
            c = read()
        }
        var x = 0
        while (c >= '0'.code && c <= '9'.code) {
            x = x * 10 + (c - '0'.code)
            c = read()
        }
        return if (neg) -x else x
    }
}

fun modPow(base: Long, exp: Int): Long {
    var b = base % MOD
    var e = exp
    var result = 1L
    while (e > 0) {
        if (e and 1 == 1) result = (result * b) % MOD
        b = (b * b) % MOD
        e = e ushr 1
    }
    return result
}

fun main() {
    val reader = FastReader()
    val bw = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.nextInt()
    val q = reader.nextInt()

    val parent = IntArray(n + 1)
    for (i in 2..n) {
        parent[i] = reader.nextInt()
    }

    val children = Array(n + 1) { IntArray(2) }
    val childCnt = IntArray(n + 1)
    for (v in 2..n) {
        val p = parent[v]
        children[p][childCnt[p]] = v
        childCnt[p]++
    }

    val spf = IntArray(MAXX + 1)
    for (i in 2..MAXX) {
        if (spf[i] == 0) {
            var j = i
            while (j <= MAXX) {
                if (spf[j] == 0) spf[j] = i
                j += i
            }
        }
    }

    val factorMap = arrayOfNulls<HashMap<Int, Int>>(n + 1)
    val interest = LongArray(n + 1) { 1L }

    val changed = mutableListOf<Pair<Int, Int>>()
    val nextChanged = mutableListOf<Pair<Int, Int>>()

    repeat(q) {
        when (reader.nextInt()) {
            1 -> {
                var v = reader.nextInt()
                var x = reader.nextInt()

                changed.clear()
                while (x > 1) {
                    val p = spf[x]
                    var cnt = 0
                    while (x % p == 0) {
                        x /= p
                        cnt++
                    }
                    var fm = factorMap[v]
                    if (fm == null) {
                        fm = HashMap()
                        factorMap[v] = fm
                    }
                    val oldExp = fm.getOrDefault(p, 0)
                    val newExp = oldExp + cnt
                    fm[p] = newExp
                    interest[v] = interest[v] * modPow(p.toLong(), cnt) % MOD
                    changed.add(p to newExp)
                }

                var child = v
                var cur = parent[v]
                while (cur != 0 && changed.isNotEmpty()) {
                    nextChanged.clear()
                    for ((p, expChild) in changed) {
                        val (l, r) = children[cur]
                        val sib = if (l == child) r else l
                        val expSib = factorMap[sib]?.getOrDefault(p, 0) ?: 0
                        val oldCur = factorMap[cur]?.getOrDefault(p, 0) ?: 0
                        val newCur = if (expChild < expSib) expChild else expSib
                        if (newCur > oldCur) {
                            var cm = factorMap[cur]
                            if (cm == null) {
                                cm = HashMap()
                                factorMap[cur] = cm
                            }
                            cm[p] = newCur
                            interest[cur] = interest[cur] * modPow(p.toLong(), newCur - oldCur) % MOD
                            nextChanged.add(p to newCur)
                        }
                    }
                    child = cur
                    cur = parent[cur]
                    changed.clear()
                    changed.addAll(nextChanged)
                }
            }
            else -> {
                val v = reader.nextInt()
                bw.write(interest[v].toString())
                bw.newLine()
            }
        }
    }

    bw.flush()
}
