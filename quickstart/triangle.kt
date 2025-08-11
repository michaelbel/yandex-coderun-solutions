import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val sides = List(3) { reader.readLine().toInt() }.sorted()

    if (sides[0] + sides[1] > sides[2]) {
        writer.write("YES")
    } else {
        writer.write("NO")
    }

    reader.close()
    writer.flush()
    writer.close()
}
