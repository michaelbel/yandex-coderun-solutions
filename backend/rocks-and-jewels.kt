import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val j = reader.readLine()
    val s = reader.readLine()
    val jewels = j.toSet()
    val count = s.count { it in jewels }
    writer.write(count.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
