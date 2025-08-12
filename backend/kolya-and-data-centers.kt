import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min
import kotlin.math.max

data class Node(val prod: Long, val idx: Int)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val m = firstLine[1].toInt()
    val q = firstLine[2].toInt()
    val R = IntArray(n) { 0 }
    val A = IntArray(n) { m }
    val prod = LongArray(n) { 0L }
    val version = IntArray(n) { 1 }
    val disabled = Array(n) { IntArray(m) { 0 } }
    var size = 1
    while (size < n) size *= 2
    val identityMax = Node(-1L, n)
    val identityMin = Node(1_000_000_000_000_000L, n)
    val segMax = Array(2 * size) { identityMax }
    val segMin = Array(2 * size) { identityMin }
    for (i in 0 until n) {
        segMax[size + i] = Node(prod[i], i)
        segMin[size + i] = Node(prod[i], i)
    }
    for (i in n until size) {
        segMax[size + i] = identityMax
        segMin[size + i] = identityMin
    }
    fun mergeMax(a: Node, b: Node): Node =
        when {
            a.prod > b.prod -> a
            a.prod < b.prod -> b
            else -> if (a.idx < b.idx) a else b
        }
    fun mergeMin(a: Node, b: Node): Node =
        when {
            a.prod < b.prod -> a
            a.prod > b.prod -> b
            else -> if (a.idx < b.idx) a else b
        }
    for (i in size - 1 downTo 1) {
        segMax[i] = mergeMax(segMax[2 * i], segMax[2 * i + 1])
        segMin[i] = mergeMin(segMin[2 * i], segMin[2 * i + 1])
    }
    fun update(index: Int, value: Long) {
        var pos = index + size
        segMax[pos] = Node(value, index)
        segMin[pos] = Node(value, index)
        pos /= 2
        while (pos >= 1) {
            segMax[pos] = mergeMax(segMax[2 * pos], segMax[2 * pos + 1])
            segMin[pos] = mergeMin(segMin[2 * pos], segMin[2 * pos + 1])
            pos /= 2
        }
    }
    repeat(q) {
        val parts = reader.readLine().split(" ")
        when (parts[0]) {
            "RESET" -> {
                val iDC = parts[1].toInt() - 1
                R[iDC]++
                version[iDC]++
                A[iDC] = m
                prod[iDC] = R[iDC].toLong() * A[iDC].toLong()
                update(iDC, prod[iDC])
            }
            "DISABLE" -> {
                val iDC = parts[1].toInt() - 1
                val jServer = parts[2].toInt() - 1
                if (disabled[iDC][jServer] != version[iDC]) {
                    disabled[iDC][jServer] = version[iDC]
                    if (A[iDC] > 0) {
                        A[iDC]--
                        prod[iDC] = R[iDC].toLong() * A[iDC].toLong()
                        update(iDC, prod[iDC])
                    }
                }
            }
            "GETMAX" -> {
                writer.write("${segMax[1].idx + 1}")
                writer.newLine()
            }
            "GETMIN" -> {
                writer.write("${segMin[1].idx + 1}")
                writer.newLine()
            }
        }
    }
    writer.flush()
    writer.close()
    reader.close()
}
