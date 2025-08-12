import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val times = mutableListOf<Int>()
    repeat(n) {
        val (hh, mm, ss) = reader.readLine().split(":").map { it.toInt() }
        val seconds = hh * 3600 + mm * 60 + ss
        times.add(seconds)
    }
    var days = 1
    for (i in 1 until n) {
        if (times[i] <= times[i - 1]) {
            days++
        }
    }
    writer.write(days.toString())
    reader.close()
    writer.close()
}
