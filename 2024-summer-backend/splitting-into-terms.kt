import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayList
import kotlin.math.min

private val writer = BufferedWriter(OutputStreamWriter(System.out))
private val currentPartition = ArrayList<Int>()

private fun generatePartitions(target: Int, maxTerm: Int) {
    if (target == 0) {
        writer.write(currentPartition.joinToString(" + "))
        writer.newLine()
        return
    }
    for (i in 1..min(target, maxTerm)) {
        currentPartition.add(i)
        generatePartitions(target - i, i)
        currentPartition.removeAt(currentPartition.size - 1)
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val n = reader.readLine().toInt()
    generatePartitions(n, n)
    reader.close()
    writer.flush()
    writer.close()
}
