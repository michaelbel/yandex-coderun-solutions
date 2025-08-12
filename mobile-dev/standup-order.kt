import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val k = reader.readLine().toInt()
    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val suffixSums = LongArray(n + 1)
    for (i in n - 1 downTo 0) {
        suffixSums[i] = a[i].toLong() + suffixSums[i + 1]
    }
    var maxIncrease = 0L
    for (p in 1..n + 1) {
        val currentIncrease = p.toLong() * k.toLong() + suffixSums[p - 1]
        maxIncrease = max(maxIncrease, currentIncrease)
    }
    writer.write(maxIncrease.toString())
    reader.close()
    writer.close()
}
