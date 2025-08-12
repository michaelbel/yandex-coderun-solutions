import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val MOD = 1_000_000_007L

private fun add(a: Long, b: Long): Long {
    val res = a + b
    return when {
        res >= MOD -> res - MOD
        res < 0 -> res + MOD
        else -> res
    }
}

private fun mul(a: Long, b: Long): Long {
    return (a * b) % MOD
}

private fun power(base: Long, exp: Long): Long {
    var res = 1L
    var b = base % MOD
    var e = exp
    while (e > 0) {
        if (e % 2 == 1L) res = mul(res, b)
        b = mul(b, b)
        e /= 2
    }
    return res
}

private fun modInverse(n: Long): Long {
    return power(n, MOD - 2)
}

private const val MAX_M = 501
private val fact = LongArray(MAX_M)
private val invFact = LongArray(MAX_M)

private fun precomputeFactorials() {
    fact[0] = 1L
    for (i in 1 until MAX_M) fact[i] = mul(fact[i - 1], i.toLong())
    invFact[MAX_M - 1] = modInverse(fact[MAX_M - 1])
    for (i in MAX_M - 2 downTo 0) {
        invFact[i] = mul(invFact[i + 1], (i + 1).toLong())
    }
}

private fun C(n: Int, k: Int): Long {
    if (k < 0 || k > n) return 0L
    return mul(mul(fact[n], invFact[k]), invFact[n - k])
}

private const val MAX_N = 51
private const val MAX_K = 251

private var dp = Array(MAX_K) { Array(MAX_N) { LongArray(MAX_N) { 0L } } }
private var next_dp = Array(MAX_K) { Array(MAX_N) { LongArray(MAX_N) { 0L } } }

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val N = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val M = a.sum()
    val K = M / 2
    precomputeFactorials()
    dp[0][0][0] = 1L
    for (i in 1..N) {
        for (j_idx in 0..K) {
            for (p1_idx in 0..i) {
                for (p2_idx in 0..i) {
                    next_dp[j_idx][p1_idx][p2_idx] = 0L
                }
            }
        }
        val count_ai = a[i - 1]
        for (j in 0..K) {
            for (p1 in 0 until i) {
                for (p2 in 0 until i) {
                    val prev = dp[j][p1][p2]
                    if (prev == 0L) continue
                    for (k in 0..count_ai) {
                        val k2 = count_ai - k
                        if (j + k > K) continue
                        val new_j = j + k
                        val new_p1 = p1 + if (k > 0) 1 else 0
                        val new_p2 = p2 + if (k2 > 0) 1 else 0
                        val combinations = C(count_ai, k)
                        val ways = mul(prev, combinations)
                        next_dp[new_j][new_p1][new_p2] = add(next_dp[new_j][new_p1][new_p2], ways)
                    }
                }
            }
        }
        for (j_idx in 0..K) {
            for (p1_idx in 0..i) {
                for (p2_idx in 0..i) {
                    dp[j_idx][p1_idx][p2_idx] = next_dp[j_idx][p1_idx][p2_idx]
                }
            }
        }
    }
    var total_draw_ways = 0L
    for (p in 0..N) {
        total_draw_ways = add(total_draw_ways, dp[K][p][p])
    }
    val total_ways = C(M, K)
    val result = if (total_ways == 0L) 0L else mul(total_draw_ways, modInverse(total_ways))
    writer.write("$result\n")
    reader.close()
    writer.close()
}
