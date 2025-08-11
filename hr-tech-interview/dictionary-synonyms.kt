import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val dictionary = HashMap<String, String>()
    repeat(n) {
        val (s1, s2) = reader.readLine().split(" ")
        dictionary[s1] = s2
        dictionary[s2] = s1
    }

    val query = reader.readLine()
    writer.write(dictionary[query] ?: "")
    writer.newLine()

    reader.close()
    writer.close()
}