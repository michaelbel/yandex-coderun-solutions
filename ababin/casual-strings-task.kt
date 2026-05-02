import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val mod = 1_000_000_007L

    reader.readLine()
    val q = reader.readLine().toInt()
    val ss = Array<String>(q) { "" }
    var maxLen = 0
    for (i in 0 until q) {
        val s = reader.readLine()
        ss[i] = s
        if (s.length > maxLen) maxLen = s.length
    }

    val fact = LongArray(maxLen + 1)
    val invFact = LongArray(maxLen + 1)
    fact[0] = 1L
    for (i in 1..maxLen) {
        fact[i] = fact[i - 1] * i % mod
    }
    invFact[maxLen] = powMod(fact[maxLen], mod - 2, mod)
    for (i in maxLen downTo 1) {
        invFact[i - 1] = invFact[i] * i % mod
    }

    val inv = LongArray(maxLen + 1)
    inv[1] = 1L
    for (i in 2..maxLen) {
        inv[i] = (mod - (mod / i) * inv[(mod % i).toInt()] % mod) % mod
    }

    fun computeTau(
        idxMap: List<Int>,
        cMat: Array<IntArray>,
        root: Int
    ): Long {
        val r = idxMap.size
        if (r <= 1) return 1L
        val L = Array(r) { LongArray(r) }
        for (i in 0 until r) {
            val vi = idxMap[i]
            var sumIn = 0
            for (k in 0 until r) {
                val vk = idxMap[k]
                if (vk != vi) {
                    sumIn += cMat[vk][vi]
                }
            }
            L[i][i] = sumIn.toLong() % mod
            for (j in 0 until r) {
                if (i == j) continue
                val vj = idxMap[j]
                L[i][j] = (mod - cMat[vj][vi] % mod) % mod
            }
        }
        val ridx = idxMap.indexOf(root)
        val size = r - 1
        if (size == 1) {
            val i = if (ridx == 0) 1 else 0
            return L[i][i] % mod
        }
        val rows = mutableListOf<Int>()
        for (i in 0 until r) if (i != ridx) rows.add(i)
        val a = L[rows[0]][rows[0]]
        val b = L[rows[0]][rows[1]]
        val c = L[rows[1]][rows[0]]
        val d = L[rows[1]][rows[1]]
        return ((a * d % mod - b * c % mod) + mod) % mod
    }

    for (s in ss) {
        val n = s.length
        if (n == 1) {
            writer.write("3\n")
            continue
        }
        val c = Array(3) { IntArray(3) }
        for (i in 0 until n - 1) {
            val u = s[i] - 'a'
            val v = s[i + 1] - 'a'
            c[u][v]++
        }
        val out = IntArray(3)
        val indeg = IntArray(3)
        for (i in 0..2) {
            for (j in 0..2) {
                out[i] += c[i][j]
                indeg[j] += c[i][j]
            }
        }
        val present = mutableListOf<Int>()
        for (i in 0..2) {
            if (out[i] + indeg[i] > 0) present.add(i)
        }
        val delta = IntArray(3) { out[it] - indeg[it] }
        var invDenom = 1L
        for (i in 0..2) for (j in 0..2) {
            invDenom = invDenom * invFact[c[i][j]] % mod
        }
        if (present.all { delta[it] == 0 }) {
            var ans = 0L
            val r = present.size
            val cExcl = Array(3) { IntArray(3) }
            for (i in 0..2) for (j in 0..2) {
                cExcl[i][j] = if (i != j) c[i][j] else 0
            }
            for (s0 in present) {
                val tau = computeTau(present, cExcl, s0)
                var part = tau * fact[out[s0]] % mod
                for (v in present) {
                    if (v != s0) {
                        part = part * fact[out[v] - 1] % mod
                    }
                }
                ans = (ans + part) % mod
            }
            ans = ans * invDenom % mod
            writer.write("$ans\n")
        } else {
            val u = present.find { delta[it] == 1 }
            val v = present.find { delta[it] == -1 }
            if (u == null || v == null) {
                writer.write("0\n")
            } else {
                val cP = Array(3) { IntArray(3) }
                for (i in 0..2) for (j in 0..2) {
                    cP[i][j] = c[i][j]
                }
                cP[v][u]++
                val outP = IntArray(3)
                val inP = IntArray(3)
                for (i in 0..2) for (j in 0..2) {
                    outP[i] += cP[i][j]
                    inP[j] += cP[i][j]
                }
                val cExclP = Array(3) { IntArray(3) }
                for (i in 0..2) for (j in 0..2) {
                    cExclP[i][j] = if (i != j) cP[i][j] else 0
                }
                val tauP = computeTau(present, cExclP, u)
                var part = tauP * fact[outP[u]] % mod
                for (x in present) {
                    if (x != u) {
                        part = part * fact[outP[x] - 1] % mod
                    }
                }
                val invInU = inv[inP[u]]
                val labeledTrails = part * invInU % mod
                val ans = labeledTrails * invDenom % mod
                writer.write("$ans\n")
            }
        }
    }
    writer.flush()
    reader.close()
    writer.close()
}

fun powMod(base: Long, exp: Long, mod: Long): Long {
    var b = base % mod
    var e = exp
    var result = 1L
    while (e > 0) {
        if (e and 1L == 1L) {
            result = (result * b) % mod
        }
        b = (b * b) % mod
        e = e shr 1
    }
    return result
}
