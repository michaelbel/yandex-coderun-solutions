import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val prices = listOf(0L) + reader.readLine().split(" ").map { it.toLong() }
    val comboPriceX = reader.readLine().toLong()
    val comboItems = reader.readLine().split(" ").map { it.toInt() }
    val k = reader.readLine().toInt()
    val desiredItemsInput = reader.readLine().split(" ").map { it.toInt() }

    val neededCount = IntArray(n + 1)
    for (itemIndex in desiredItemsInput) {
        if (itemIndex in 1..n) {
            neededCount[itemIndex]++
        }
    }

    var minTotalCost = Long.MAX_VALUE
    val maxCombosToConsider = k + 1

    for (numCombos in 0..maxCombosToConsider) {
        var currentCost = numCombos * comboPriceX
        val remainingNeeded = neededCount.clone()
        for (comboItemIndex in comboItems) {
            remainingNeeded[comboItemIndex] = max(0, remainingNeeded[comboItemIndex] - numCombos)
        }
        var individualCost = 0L
        for (itemIndex in 1..n) {
            individualCost += remainingNeeded[itemIndex] * prices[itemIndex]
        }
        minTotalCost = min(minTotalCost, currentCost + individualCost)
    }

    writer.write(minTotalCost.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
