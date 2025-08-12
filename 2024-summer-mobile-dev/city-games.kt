import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val line1 = reader.readLine()
    val line2 = reader.readLine()
    val pairs1 = line1.removeSuffix(".").split(", ").map { it.split(" ") }
    val pairs2 = line2.removeSuffix(".").split(", ").map { it.split(" ") }
    val set1 = pairs1.map { setOf(it[0], it[1]) }.toSet()
    val set2 = pairs2.map { setOf(it[0], it[1]) }.toSet()
    if (set1 == set2) {
        writer.write("YES")
    } else {
        writer.write("NO")
    }
    reader.close()
    writer.close()
}
