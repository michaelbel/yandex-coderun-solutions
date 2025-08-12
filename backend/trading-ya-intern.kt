import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, m) = reader.readLine().split(" ").map { it.toInt() }
    val a = reader.readLine().split(" ").map { it.toLong() }.sorted()
    val b = reader.readLine().split(" ").map { it.toLong() }.sortedDescending()
    var profit = 0L
    var i = 0
    var j = 0
    while (i < n && j < m) {
        if (a[i] < b[j]) {
            profit += b[j] - a[i]
            i++
            j++
        } else {
            break
        }
    }
    writer.write(profit.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
