import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = reader.readLine().split(" ")
    val m = firstLine[1].toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val nonZeros = mutableListOf<Pair<Int, Long>>()
    for (i in 0 until m) {
        if (a[i] > 0) {
            nonZeros.add(Pair(i, a[i].toLong()))
        }
    }
    val nzCount = nonZeros.size
    val nzPrefixSum = LongArray(nzCount + 1) { 0L }
    for (k in 0 until nzCount) {
        nzPrefixSum[k + 1] = nzPrefixSum[k] + nonZeros[k].second
    }
    var totalScore = 0L
    var nzPtr = 0
    for (i in 0 until m) {
        val currentScoreInt = a[i]
        if (currentScoreInt == 0) continue
        val currentScoreLong = currentScoreInt.toLong()
        totalScore += currentScoreLong * currentScoreLong
        if (i < m - 1) {
            while (nzPtr < nzCount && nonZeros[nzPtr].first <= i) {
                nzPtr++
            }
            val startNzBonusIndex = nzPtr
            val count = currentScoreInt
            val endNzBonusIndex = min(startNzBonusIndex + count, nzCount)
            if (startNzBonusIndex < endNzBonusIndex) {
                val bonus = nzPrefixSum[endNzBonusIndex] - nzPrefixSum[startNzBonusIndex]
                totalScore += bonus
            }
        }
    }
    writer.write(totalScore.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
