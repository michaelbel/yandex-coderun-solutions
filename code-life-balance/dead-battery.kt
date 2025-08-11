import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val totalPerHour = a.sum()
    val hours = 100 / totalPerHour
    writer.write(hours.toString())

    reader.close()
    writer.close()
}
