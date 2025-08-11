import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val shirts = reader.readLine().split(" ").map { it.toLong() }.toLongArray()

    val m = reader.readLine().toInt()
    val pants = reader.readLine().split(" ").map { it.toLong() }.toLongArray()

    var i = 0
    var j = 0
    var minDiff = Long.MAX_VALUE
    var bestShirt = 0L
    var bestPants = 0L

    while (i < n && j < m) {
        val diff = kotlin.math.abs(shirts[i] - pants[j])
        if (diff < minDiff) {
            minDiff = diff
            bestShirt = shirts[i]
            bestPants = pants[j]
        }
        if (shirts[i] < pants[j]) {
            i++
        } else {
            j++
        }
    }

    writer.write("$bestShirt $bestPants")
    writer.newLine()

    reader.close()
    writer.close()
}
