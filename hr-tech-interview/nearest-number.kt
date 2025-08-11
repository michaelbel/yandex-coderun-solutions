import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.abs

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val numbers = reader.readLine().split(" ").map(String::toInt)
    val x = reader.readLine().toInt()

    var closest = numbers[0]
    var minDiff = abs(numbers[0] - x)

    for (num in numbers) {
        val diff = abs(num - x)
        if (diff < minDiff) {
            closest = num
            minDiff = diff
        }
    }

    writer.write(closest.toString())

    reader.close()
    writer.flush()
    writer.close()
}