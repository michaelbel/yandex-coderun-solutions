import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val stInit = StringTokenizer(reader.readLine())
    val n = stInit.nextToken().toInt()
    val k = stInit.nextToken().toLong()
    val a = LongArray(n)
    val b = LongArray(n)
    val c = IntArray(n)
    repeat(n) { i ->
        val st = StringTokenizer(reader.readLine())
        a[i] = st.nextToken().toLong()
        b[i] = st.nextToken().toLong()
        c[i] = st.nextToken().toInt()
    }
    val mod = 1_000_000_007L
    var dp = LongArray(c[0] + 1) { 0L }
    dp[0] = 1L

    fun identityMatrix(d: Int): Array<LongArray> {
        val I = Array(d) { LongArray(d) }
        for (i in 0 until d) I[i][i] = 1L
        return I
    }

    fun multiplyMatrices(
        A: Array<LongArray>,
        B: Array<LongArray>,
        mod: Long
    ): Array<LongArray> {
        val d = A.size
        val C = Array(d) { LongArray(d) }
        for (i in 0 until d) {
            for (k in 0 until d) {
                val aVal = A[i][k]
                if (aVal != 0L) {
                    for (j in 0 until d) {
                        C[i][j] = (C[i][j] + aVal * B[k][j] % mod) % mod
                    }
                }
            }
        }
        return C
    }

    for (i in 0 until n) {
        val cap = c[i]
        dp = when {
            dp.size > cap + 1 -> dp.copyOf(cap + 1)
            dp.size < cap + 1 -> dp.copyOf(cap + 1).also { }
            else -> dp
        }

        val length = if (i < n - 1) (b[i] - a[i]) else (k - a[i])
        if (length <= 0L) continue

        val d = cap + 1
        var M = Array(d) { LongArray(d) }
        for (y in 0 until d) {
            M[y][y] = (M[y][y] + 1L)
            if (y < d - 1) M[y][y + 1] = (M[y][y + 1] + 1L)
            if (y > 0) M[y][y - 1] = (M[y][y - 1] + 1L)
        }

        var P = identityMatrix(d)
        var exp = length
        while (exp > 0) {
            if (exp and 1L == 1L) {
                P = multiplyMatrices(P, M, mod)
            }
            M = multiplyMatrices(M, M, mod)
            exp = exp shr 1
        }

        val newDp = LongArray(d)
        for (y in 0 until d) {
            var sum = 0L
            for (j in 0 until d) {
                sum = (sum + P[y][j] * dp[j] % mod) % mod
            }
            newDp[y] = sum
        }
        dp = newDp
    }

    val writer = BufferedWriter(OutputStreamWriter(System.out))
    writer.write((dp.getOrElse(0) { 0L } % mod).toString())
    writer.newLine()
    writer.flush()
}
