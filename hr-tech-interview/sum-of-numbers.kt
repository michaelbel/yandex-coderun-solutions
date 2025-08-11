import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (n, k) = reader.readLine().split(" ").map { it.toInt() }
    val arr = reader.readLine().split(" ").map { it.toInt() }

    var currentSum = 0L
    var count = 0
    val prefixSumCount = mutableMapOf<Long, Int>()
    prefixSumCount[0L] = 1

    for (num in arr) {
        currentSum += num.toLong()
        val target = currentSum - k.toLong()
        count += prefixSumCount.getOrDefault(target, 0)
        prefixSumCount[currentSum] = prefixSumCount.getOrDefault(currentSum, 0) + 1
    }

    writer.write(count.toString())
    writer.newLine()

    reader.close()
    writer.close()
}