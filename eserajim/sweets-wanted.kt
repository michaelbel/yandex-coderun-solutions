import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    if (n < 3) {
        writer.write("0")
        writer.flush()
        reader.close()
        writer.close()
        return
    }

    val tokens = reader.readLine().split(" ").map { it.toLong() }
    val arr = tokens.toMutableList()
    arr.sort()

    val prefix = LongArray(n)
    prefix[0] = arr[0]
    for (i in 1 until n) {
        prefix[i] = prefix[i - 1] + arr[i]
    }

    val maxVal = arr[n - 1]
    var best = n * maxVal - prefix[n - 1]

    var i = 0
    while (i < n) {
        val candidate = arr[i]
        if (candidate == maxVal) break
        var j = i
        while (j + 1 < n && arr[j + 1] == candidate) {
            j++
        }

        val countOptional = j + 1
        val sumOptional = prefix[j]
        val costOptional = countOptional * candidate - sumOptional

        val countForced = n - countOptional
        val sumForced = prefix[n - 1] - sumOptional
        val costForced = countForced * maxVal - sumForced

        val totalCost = costOptional + costForced
        if (totalCost < best) best = totalCost

        i = j + 1
    }

    writer.write(best.toString())
    writer.flush()
    reader.close()
    writer.close()
}
