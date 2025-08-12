import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    var left = 0
    var right = 1000
    var result = 0
    while (left <= right) {
        val mid = (left + right) / 2
        val threshold = mid * mid
        val count = a.count { it >= threshold }
        if (count >= mid) {
            result = mid
            left = mid + 1
        } else {
            right = mid - 1
        }
    }
    writer.write(result.toString())
    reader.close()
    writer.close()
}
