import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val q = reader.readLine().toInt()
    val queries = IntArray(q)
    var maxN = 0
    for (i in 0 until q) {
        queries[i] = reader.readLine().toInt()
        if (queries[i] > maxN) {
            maxN = queries[i]
        }
    }
    val squares = mutableListOf<Int>()
    var i = 1
    while (i * i <= maxN) {
        squares.add(i * i)
        i++
    }
    val dp = BooleanArray(maxN + 1)
    dp[0] = false
    for (n in 1..maxN) {
        for (sq in squares) {
            if (sq > n) break
            if (!dp[n - sq]) {
                dp[n] = true
                break
            }
        }
    }
    val sb = StringBuilder()
    for (n in queries) {
        sb.append(if (dp[n]) "1" else "0").append("\n")
    }
    writer.write(sb.toString())
    writer.flush()
    reader.close()
    writer.close()
}
