private const val MOD = 1_000_000_007L

private fun modPow(a: Long, e: Long): Long {
    var base = (a % MOD + MOD) % MOD
    var exp = e
    var res = 1L
    while (exp > 0) {
        if ((exp and 1L) != 0L) res = (res * base) % MOD
        base = (base * base) % MOD
        exp = exp shr 1
    }
    return res
}

private fun modInv(x: Long): Long = modPow(x, MOD - 2)

fun solve(a: Int, k: Int, n: Int): Int {
    val r = kotlin.math.min(k, n)
    val N = k.toLong() + n.toLong()
    var numer = 1L
    var denom = 1L
    var i = 1
    while (i <= r) {
        val term = (N - r + i).toLong() % MOD
        numer = (numer * term) % MOD
        denom = (denom * i.toLong()) % MOD
        i++
    }
    val binom = (numer * modInv(denom)) % MOD
    val ans = modInv(binom)
    return ans.toInt()
}
