import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine()
    val c = reader.readLine()
    val targetSet = c.toSet()
    var minLength = Int.MAX_VALUE
    var found = false
    for (i in 0 until s.length) {
        for (j in i until s.length) {
            val sub = s.substring(i, j + 1)
            val substringSet = sub.toSet()
            if (substringSet == targetSet) {
                val currentLength = j - i + 1
                minLength = min(minLength, currentLength)
                found = true
            }
        }
    }
    if (found) {
        writer.write(minLength.toString())
    } else {
        writer.write("0")
    }
    writer.newLine()
    reader.close()
    writer.close()
}
