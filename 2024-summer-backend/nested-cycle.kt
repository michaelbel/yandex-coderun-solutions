import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val mod = 1_000_000_007
    val N = reader.readLine().toInt()
    val lIsVar = BooleanArray(N + 1)
    val rIsVar = BooleanArray(N + 1)
    val lConst = IntArray(N + 1)
    val rConst = IntArray(N + 1)
    val lVar = IntArray(N + 1)
    val rVar = IntArray(N + 1)
    repeat(N) { i0 ->
        val i = i0 + 1
        val parts = reader.readLine().split(' ')
        val u = parts[0]
        val v = parts[1]
        if (u[0].isLowerCase()) {
            lIsVar[i] = true
            lVar[i] = u[0] - 'a' + 1
        } else {
            lConst[i] = u.toInt()
        }
        if (v[0].isLowerCase()) {
            rIsVar[i] = true
            rVar[i] = v[0] - 'a' + 1
        } else {
            rConst[i] = v.toInt()
        }
    }
    val parent = IntArray(N + 1)
    val children = Array(N + 1) { mutableListOf<Int>() }
    for (i in 1..N) {
        parent[i] = when {
            lIsVar[i] -> lVar[i]
            rIsVar[i] -> rVar[i]
            else       -> 0
        }
        if (parent[i] != 0) {
            children[parent[i]].add(i)
        }
    }
    val domainLo = IntArray(N + 1)
    val domainHi = IntArray(N + 1)
    for (i in 1..N) {
        domainLo[i] = if (lIsVar[i]) domainLo[lVar[i]] else lConst[i]
        domainHi[i] = if (rIsVar[i]) domainHi[rVar[i]] else rConst[i]
        if (domainLo[i] > domainHi[i]) {
            writer.write("0\n")
            writer.flush()
            return
        }
    }
    val g = arrayOfNulls<IntArray>(N + 1)
    for (j in N downTo 1) {
        val loJ = domainLo[j]
        val hiJ = domainHi[j]
        val sizeJ = hiJ - loJ + 1
        val f = IntArray(sizeJ)
        if (children[j].isEmpty()) {
            for (idx in 0 until sizeJ) f[idx] = 1
        } else {
            for (idx in 0 until sizeJ) {
                var prod = 1L
                for (c in children[j]) {
                    val gC = g[c]!!
                    prod = prod * gC[idx] % mod
                }
                f[idx] = prod.toInt()
            }
        }
        val pf = IntArray(sizeJ)
        if (sizeJ > 0) {
            pf[0] = f[0]
            for (k in 1 until sizeJ) {
                val s = pf[k - 1] + f[k]
                pf[k] = if (s >= mod) s - mod else s
            }
        }
        val p = parent[j]
        if (p != 0) {
            val loP = domainLo[p]
            val hiP = domainHi[p]
            val sizeP = hiP - loP + 1
            val gj = IntArray(sizeP)
            for (pIdx in 0 until sizeP) {
                val pval = loP + pIdx
                var lb = if (lIsVar[j]) pval else lConst[j]
                var ub = if (rIsVar[j]) pval else rConst[j]
                if (lb < loJ) lb = loJ
                if (ub > hiJ) ub = hiJ
                if (lb > ub) {
                    gj[pIdx] = 0
                } else {
                    val left = lb - loJ
                    val right = ub - loJ
                    val sumUpper = pf[right]
                    val sumLower = if (left > 0) pf[left - 1] else 0
                    var v = sumUpper - sumLower
                    if (v < 0) v += mod
                    gj[pIdx] = v
                }
            }
            g[j] = gj
        }
    }
    var result = 1L
    for (i in 1..N) {
        if (parent[i] == 0) {
            val loI = domainLo[i]
            val hiI = domainHi[i]
            val sizeI = hiI - loI + 1
            if (children[i].isEmpty()) {
                result = result * sizeI % mod
            } else {
                var sumTree = 0L
                for (idx in 0 until sizeI) {
                    var prod = 1L
                    for (c in children[i]) {
                        val gC = g[c]!!
                        prod = prod * gC[idx] % mod
                    }
                    sumTree = (sumTree + prod) % mod
                }
                result = result * sumTree % mod
            }
        }
    }
    writer.write("$result\n")
    writer.flush()
}
