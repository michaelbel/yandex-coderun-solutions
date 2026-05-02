import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().trim().toInt()

    val dp = LongArray(n + 3)
    dp[0] = 1L

    for (i in 1..n) {
        var ways = dp[i - 1]
        if (i >= 2) ways += dp[i - 2]
        if (i >= 3) ways += dp[i - 3]
        dp[i] = ways
    }

    writer.write(dp[n].toString())
    writer.newLine()
    writer.flush()
}
