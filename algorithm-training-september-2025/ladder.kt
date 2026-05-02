import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().trim().toInt()

    val dp = LongArray(n + 1)
    dp[0] = 1L

    for (part in 1..n) {
        for (sum in n downTo part) {
            dp[sum] += dp[sum - part]
        }
    }

    writer.write(dp[n].toString())
    writer.newLine()
    writer.flush()
}
