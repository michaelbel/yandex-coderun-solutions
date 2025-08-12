import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Collections
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.sqrt

fun getDivisors(m: Long): Pair<List<Long>, Map<Long, Int>> {
    if (m <= 0L) return Pair(emptyList(), emptyMap())
    val divisors = HashSet<Long>()
    divisors.add(1L)
    val limit = sqrt(m.toDouble()).toLong()
    for (i in 1..limit) {
        if (m % i == 0L) {
            divisors.add(i)
            divisors.add(m / i)
        }
    }
    if (m > 1) divisors.add(m)
    val sortedDivisors = ArrayList(divisors)
    Collections.sort(sortedDivisors)
    val divisorToId = HashMap<Long, Int>()
    for (i in sortedDivisors.indices) {
        divisorToId[sortedDivisors[i]] = i
    }
    return Pair(sortedDivisors, divisorToId)
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLineParts = reader.readLine().split(" ")
    val n = firstLineParts[0].toInt()
    val m = firstLineParts[1].toLong()
    val k = firstLineParts[2].toInt()
    val a = reader.readLine().split(" ").map { it.toLong() }
    val resultIndices = ArrayList<Int>()
    if (m == 0L) {
        var zeroIdx = -1
        for (i in 0 until n) {
            if (a[i] == 0L) {
                zeroIdx = i + 1
                break
            }
        }
        if (zeroIdx != -1) {
            resultIndices.add(zeroIdx)
            var count = 1
            for (i in 0 until n) {
                if (count < k && (i + 1) != zeroIdx) {
                    resultIndices.add(i + 1)
                    count++
                }
            }
        }
    } else if (m == 1L) {
        var count = 0
        for (i in 0 until n) {
            if (a[i] == 1L && count < k) {
                resultIndices.add(i + 1)
                count++
            }
        }
    } else {
        val (mDivisors, divisorToId) = getDivisors(m)
        val divisorsList = ArrayList<Pair<Long, Int>>()
        val onesIndices = ArrayList<Int>()
        for (i in 0 until n) {
            if (a[i] == 1L) {
                onesIndices.add(i + 1)
            } else if (a[i] > 0L && m % a[i] == 0L) {
                divisorsList.add(Pair(a[i], i + 1))
            }
        }
        val nDiv = divisorsList.size
        val dp = Array(k + 1) { HashSet<Int>() }
        val parent = HashMap<Pair<Int, Int>, Pair<Int, Int>>()
        val idOf1 = divisorToId[1L]
        if (idOf1 != null) {
            dp[0].add(idOf1)
            parent[Pair(0, idOf1)] = Pair(-1, -1)
        }
        for (i in 0 until nDiv) {
            val (value, index) = divisorsList[i]
            val newlyReached = HashMap<Int, HashSet<Int>>()
            for (j in k - 1 downTo 0) {
                if (dp[j].isNotEmpty()) {
                    for (dId in dp[j]) {
                        val currentProd = mDivisors[dId]
                        if (value > 0 && currentProd > m / value) continue
                        val nextProd = currentProd * value
                        val nextDId = divisorToId[nextProd]
                        if (nextDId != null) {
                            val nextJ = j + 1
                            if (nextJ <= k) {
                                if (nextDId !in dp[nextJ] && nextDId !in newlyReached.getOrDefault(nextJ, HashSet())) {
                                    newlyReached.getOrPut(nextJ) { HashSet() }.add(nextDId)
                                    parent[Pair(nextJ, nextDId)] = Pair(dId, index)
                                }
                            }
                        }
                    }
                }
            }
            for ((jNew, dIdsNew) in newlyReached) {
                dp[jNew].addAll(dIdsNew)
            }
        }
        val targetDId = divisorToId[m]
        if (targetDId != null) {
            for (kDiv in k downTo 0) {
                if (targetDId in dp[kDiv]) {
                    val kOne = k - kDiv
                    if (onesIndices.size >= kOne) {
                        var currentJ = kDiv
                        var currentDId = targetDId
                        val usedDivisorIndices = ArrayList<Int>()
                        while (currentJ > 0) {
                            val parentInfo = parent[Pair(currentJ, currentDId)] ?: throw IllegalStateException()
                            val prevDId = parentInfo.first
                            val originalIndex = parentInfo.second
                            usedDivisorIndices.add(originalIndex)
                            currentDId = prevDId
                            currentJ--
                        }
                        resultIndices.addAll(usedDivisorIndices)
                        for (i in 0 until kOne) {
                            resultIndices.add(onesIndices[i])
                        }
                        break
                    }
                }
            }
        }
        if (resultIndices.size != k) {
            throw IllegalStateException("Failed to find a solution, but one is guaranteed.")
        }
    }
    writer.write(resultIndices.joinToString(" "))
    writer.newLine()
    reader.close()
    writer.close()
}
