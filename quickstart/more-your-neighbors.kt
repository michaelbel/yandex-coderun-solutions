import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val numbers = reader.readLine().split(" ").map(String::toInt)
    var count = 0

    for (i in 1 until numbers.size - 1) {
        if (numbers[i] > numbers[i - 1] && numbers[i] > numbers[i + 1]) {
            count++
        }
    }

    writer.write(count.toString())

    reader.close()
    writer.flush()
    writer.close()
}
