import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val s = reader.readLine()
    val a = reader.readLine().split(" ").map(String::toInt)
    var maxTime = a[0]
    var maxChar = s[0]
    for (i in 1 until n) {
        val searchTime = a[i] - a[i - 1]
        if (searchTime >= maxTime) {
            maxTime = searchTime
            maxChar = s[i]
        }
    }
    writer.write(maxChar.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
