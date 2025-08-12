import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val arr = reader.readLine().split(" ").map { it.toInt() }
    var allOnes = true
    for (num in arr) {
        if (num != 1) {
            allOnes = false
            break
        }
    }
    var result = 0L
    if (allOnes) {
        result = n.toLong() * (n + 1) / 2
    } else {
        var count = 0L
        for (num in arr) {
            if (num == 1) {
                result += count * (count + 1) / 2
                count = 0L
            } else {
                count++
            }
        }
        result += count * (count + 1) / 2
    }
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
    writer.close()
    reader.close()
}
