fun solution(n: Long): Long {
    val MOD = 1_000_000_000L - 7_538L
    val memo = HashMap<Long, Long>().apply { this[0L] = 1L }

    fun powMod(baseInit: Long, expInit: Long): Long {
        var base = baseInit % MOD
        var e = expInit
        var res = 1L
        while (e > 0) {
            if ((e and 1L) != 0L) res = (res * base) % MOD
            base = (base * base) % MOD
            e = e shr 1
        }
        return res
    }

    fun dfs(x: Long): Long {
        return memo.getOrPut(x) {
            val a = dfs(x / 2L)
            val b = dfs(x / 3L)
            val c = dfs(x / 4L)
            val term1 = powMod(a, b)
            val term23 = ((5L * (c % MOD)) % MOD + (x % MOD)) % MOD
            (term1 + term23) % MOD
        }
    }

    return dfs(n)
}
