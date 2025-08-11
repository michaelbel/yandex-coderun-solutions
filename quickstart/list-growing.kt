import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val numbers = reader.readLine().split(" ").map(String::toInt)
    var isIncreasing = true

    for (i in 1 until numbers.size) {
        if (numbers[i] <= numbers[i - 1]) {
            isIncreasing = false
            break
        }
    }

    writer.write(if (isIncreasing) "YES" else "NO")

    reader.close()
    writer.flush()
    writer.close()
}
