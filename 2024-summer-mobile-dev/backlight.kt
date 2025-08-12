import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine()
    val n = s.length
    val sChars = s.toCharArray()
    val yandex = "Yandex"
    val cup = "Cup"
    val yandexLen = yandex.length
    val cupLen = cup.length
    val costY = IntArray(n)
    val costC = IntArray(n)
    for (i in 0..n - yandexLen) {
        var currentCost = 0
        for (k in 0 until yandexLen) {
            if (sChars[i + k] != yandex[k]) {
                currentCost++
            }
        }
        costY[i] = currentCost
    }
    for (j in 0..n - cupLen) {
        var currentCost = 0
        for (k in 0 until cupLen) {
            if (sChars[j + k] != cup[k]) {
                currentCost++
            }
        }
        costC[j] = currentCost
    }
    val minCostC = IntArray(n + 1) { Int.MAX_VALUE }
    val bestJ = IntArray(n + 1) { -1 }
    if (n >= cupLen) {
        minCostC[n - cupLen] = costC[n - cupLen]
        bestJ[n - cupLen] = n - cupLen
        for (k in n - cupLen - 1 downTo 0) {
            if (costC[k] <= minCostC[k + 1]) {
                minCostC[k] = costC[k]
                bestJ[k] = k
            } else {
                minCostC[k] = minCostC[k + 1]
                bestJ[k] = bestJ[k + 1]
            }
        }
    }
    var minTotalCost = Int.MAX_VALUE
    var finalBestI = -1
    var finalBestJ = -1
    for (i in 0..n - yandexLen) {
        val jStartIndex = i + yandexLen
        if (jStartIndex <= n - cupLen) {
            if (minCostC[jStartIndex] != Int.MAX_VALUE) {
                val currentTotalCost = costY[i] + minCostC[jStartIndex]
                if (currentTotalCost < minTotalCost) {
                    minTotalCost = currentTotalCost
                    finalBestI = i
                    finalBestJ = bestJ[jStartIndex]
                }
            }
        }
    }
    if (finalBestI != -1 && finalBestJ != -1) {
        for (k in 0 until yandexLen) {
            sChars[finalBestI + k] = yandex[k]
        }
        for (k in 0 until cupLen) {
            sChars[finalBestJ + k] = cup[k]
        }
    }
    writer.write(String(sChars))
    writer.newLine()
    reader.close()
    writer.close()
}
