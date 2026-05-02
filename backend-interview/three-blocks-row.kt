import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun countSequences(n: Int): Int {
    if (n == 1) return 2
    if (n == 2) return 4
    if (n == 3) return 7
    
    val dp = IntArray(n + 1)
    dp[1] = 2 // "0", "1"
    dp[2] = 4 // "00", "01", "10", "11"
    dp[3] = 7 // "000", "001", "010", "011", "100", "101", "110"
    
    for (i in 4..n) {
        dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3] // Рекуррентная формула
    }
    
    return dp[n]
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    writer.write(countSequences(n).toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
