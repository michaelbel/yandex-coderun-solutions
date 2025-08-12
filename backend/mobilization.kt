import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class FenwickTree(val n: Int) {
    private val tree = LongArray(n + 1) { 0L }
    fun update(i: Int, delta: Long) {
        var idx = i
        while (idx <= n) {
            tree[idx] += delta
            idx += idx and -idx
        }
    }
    fun query(i: Int): Long {
        var sum = 0L
        var idx = i
        while (idx > 0) {
            sum += tree[idx]
            idx -= idx and -idx
        }
        return sum
    }
    fun lowerBound(target: Long): Int {
        var idx = 0
        var bit = Integer.highestOneBit(n)
        var curTarget = target
        while (bit > 0) {
            val next = idx + bit
            if (next <= n && tree[next] < curTarget) {
                curTarget -= tree[next]
                idx = next
            }
            bit = bit shr 1
        }
        return idx + 1
    }
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val K = n / 2
    val aList = reader.readLine().split(" ").map { it.toLong() }
    val bList = reader.readLine().split(" ").map { it.toLong() }
    var globalSumA = aList.sum()
    val diff = LongArray(n + 1)
    for (i in 1..n)
        diff[i] = aList[i - 1] - bList[i - 1]
    val m = reader.readLine().toInt()
    val queries = Array(m) {
        val parts = reader.readLine().split(" ")
        Triple(parts[0].toInt(), parts[1].toInt(), parts[2].toLong())
    }
    val allDiffs = ArrayList<Long>()
    for (i in 1..n)
        allDiffs.add(diff[i])
    val sim = diff.copyOf()
    for (q in queries) {
        val num = q.first
        val type = q.second
        val d = q.third
        if (type == 1) {
            sim[num] = sim[num] + d
        } else {
            sim[num] = sim[num] - d
        }
        allDiffs.add(sim[num])
    }
    allDiffs.sort()
    val compList = ArrayList<Long>()
    compList.add(allDiffs[0])
    for (i in 1 until allDiffs.size) {
        if (allDiffs[i] != compList.last())
            compList.add(allDiffs[i])
    }
    val compSize = compList.size
    val compMap = HashMap<Long, Int>()
    for (i in compList.indices) {
        compMap[compList[i]] = i + 1
    }
    val bitFreq = FenwickTree(compSize)
    val bitSum = FenwickTree(compSize)
    for (i in 1..n) {
        val pos = compMap[diff[i]]!!
        bitFreq.update(pos, 1)
        bitSum.update(pos, diff[i])
    }
    fun queryBottomK(k: Int): Long {
        val idx = bitFreq.lowerBound(k.toLong())
        val countBefore = bitFreq.query(idx - 1)
        val sumBefore = bitSum.query(idx - 1)
        val remainder = k - countBefore
        val value = compList[idx - 1]
        return sumBefore + remainder * value
    }
    val output = StringBuilder()
    for (q in queries) {
        val num = q.first
        val type = q.second
        val d = q.third
        val oldVal = diff[num]
        val oldPos = compMap[oldVal]!!
        bitFreq.update(oldPos, -1)
        bitSum.update(oldPos, -oldVal)
        if (type == 1) {
            diff[num] += d
            globalSumA += d
        } else {
            diff[num] -= d
        }
        val newVal = diff[num]
        val newPos = compMap[newVal]!!
        bitFreq.update(newPos, 1)
        bitSum.update(newPos, newVal)
        val bottomSum = queryBottomK(K)
        val ans = globalSumA - bottomSum
        output.append(ans).append("\n")
    }
    writer.write(output.toString())
    writer.flush()
    reader.close()
    writer.close()
}
