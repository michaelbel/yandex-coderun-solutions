import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val heights = reader.readLine().split(" ").map { it.toInt() }

    val H = 100
    val SENTINEL_R = H + 1
    var dp = Array(H + 1) { IntArray(H + 2) { -1 } }
    dp[0][SENTINEL_R] = 0

    for (h in heights) {
        val newDp = Array(H + 1) { IntArray(H + 2) { -1 } }
        for (l in 0..H) {
            for (r in 1..SENTINEL_R) {
                if (dp[l][r] == -1) continue
                val k = dp[l][r]
                newDp[l][r] = max(newDp[l][r], k)
                if (h >= l && (r == SENTINEL_R || h <= r)) {
                    newDp[h][r] = max(newDp[h][r], k + 1)
                }
                if (h <= r && (l == 0 || h >= l)) {
                    newDp[l][h] = max(newDp[l][h], k + 1)
                }
            }
        }
        dp = newDp
    }

    var maxBooks = 0
    for (l in 0..H) {
        for (r in 1..SENTINEL_R) {
            maxBooks = max(maxBooks, dp[l][r])
        }
    }

    writer.write(maxBooks.toString())
    writer.newLine()
    writer.flush()
}

