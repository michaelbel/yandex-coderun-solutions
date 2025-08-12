import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.MutableSet
import kotlin.collections.emptyList
import kotlin.collections.mutableListOf
import kotlin.collections.mutableSetOf
import kotlin.collections.toList
import kotlin.collections.removeLast
import kotlin.collections.joinToString

lateinit var nGlobal: String
var bestPartitionGlobal: List<String> = emptyList()

fun findPartitionRecursive(index: Int, currentPartition: MutableList<String>, usedNumbers: MutableSet<String>) {
    if (index == nGlobal.length) {
        if (currentPartition.size > bestPartitionGlobal.size) {
            bestPartitionGlobal = currentPartition.toList()
        }
        return
    }
    for (k in index + 1..nGlobal.length) {
        val part = nGlobal.substring(index, k)
        if (part.length > 1 && part[0] == '0') {
            continue
        }
        if (usedNumbers.contains(part)) {
            continue
        }
        currentPartition.add(part)
        usedNumbers.add(part)
        findPartitionRecursive(k, currentPartition, usedNumbers)
        usedNumbers.remove(part)
        currentPartition.removeLast()
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine()
    nGlobal = n
    if (n == "0") {
        writer.write("0")
    } else {
        bestPartitionGlobal = listOf(n)
        findPartitionRecursive(0, mutableListOf<String>(), mutableSetOf<String>())
        writer.write(bestPartitionGlobal.joinToString("-"))
    }
    writer.newLine()
    reader.close()
    writer.close()
}
