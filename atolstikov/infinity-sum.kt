import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val parts = reader.readLine().split(" ")
    val a = parts[0].toInt()
    val b = parts[1].toInt()

    if (b == 1) {
        writer.write("infinity")
    } else {
        val S = Array(a + 1) { Array(a + 1) { BigInteger.ZERO } }
        S[0][0] = BigInteger.ONE
        for (n in 1..a) {
            for (k in 1..n) {
                S[n][k] = S[n - 1][k].multiply(BigInteger.valueOf(k.toLong()))
                    .add(S[n - 1][k - 1])
            }
        }

        val fact = Array(a + 1) { BigInteger.ONE }
        for (i in 1..a) {
            fact[i] = fact[i - 1].multiply(BigInteger.valueOf(i.toLong()))
        }

        val bMinus1 = BigInteger.valueOf(b.toLong()).subtract(BigInteger.ONE)
        val denom = bMinus1.pow(a + 1)

        var numeratorSum = BigInteger.ZERO
        for (k in 0..a) {
            val term = S[a][k]
                .multiply(fact[k])
                .multiply(bMinus1.pow(a - k))
            numeratorSum = numeratorSum.add(term)
        }
        val numerator = numeratorSum.multiply(BigInteger.valueOf(b.toLong()))

        val g = numerator.gcd(denom)
        val num = numerator.divide(g)
        val den = denom.divide(g)

        writer.write("$num/$den")
    }

    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
