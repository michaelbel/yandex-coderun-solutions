import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().trim().split(" ")
    val n = firstLine[0].toInt()
    val k = firstLine[1].toInt()
    val a = reader.readLine().trim().split(" ").map { it.toInt() }.toIntArray()
    if (n == 1) {
        writer.write(a[0].toString())
        writer.newLine()
        writer.flush()
        return
    }
    var low = if (a[0] > a[n - 1]) a[0] else a[n - 1]
    var high = a.maxOrNull()!!
    fun canReach(L: Int): Boolean {
        if (a[0] > L || a[n - 1] > L) return false
        val dp = IntArray(n)
        dp[0] = 1
        var window = 1
        for (i in 1 until n) {
            if (i > k) {
                window -= dp[i - k - 1]
            }
            if (a[i] <= L && window > 0) {
                dp[i] = 1
            } else {
                dp[i] = 0
            }
            window += dp[i]
        }
        return dp[n - 1] == 1
    }
    while (low < high) {
        val mid = low + (high - low) / 2
        if (canReach(mid)) {
            high = mid
        } else {
            low = mid + 1
        }
    }
    writer.write(low.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
