import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

var minTotalCost = Double.MAX_VALUE
var bestCouponIndices = listOf<Int>()
lateinit var costs: List<Int>
lateinit var applicableCouponsPerItem: List<Set<Int>>
lateinit var discountMultipliers: List<Double>
var nItems = 0
var nCoupons = 0

fun calculateTotalCost(chosen: Set<Int>): Double {
    var total = 0.0
    for (i in 0 until nItems) {
        var itemCost = costs[i].toDouble()
        val applicable = applicableCouponsPerItem[i]
        for (coupon in chosen) {
            if (applicable.contains(coupon)) {
                itemCost *= discountMultipliers[coupon]
            }
        }
        total += itemCost
    }
    return total
}

fun findBestCombination(start: Int, current: MutableList<Int>, maxCoupons: Int) {
    val currentCost = calculateTotalCost(current.toSet())
    if (currentCost < minTotalCost) {
        minTotalCost = currentCost
        bestCouponIndices = current.toList()
    }
    if (current.size < maxCoupons) {
        for (i in start until nCoupons) {
            current.add(i)
            findBestCombination(i + 1, current, maxCoupons)
            current.removeAt(current.size - 1)
        }
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val input = reader.readLine().split(" ")
    val n = input[0].toInt()
    val m = input[1].toInt()
    val k = input[2].toInt()
    nItems = n
    nCoupons = m
    costs = reader.readLine().split(" ").map { it.toInt() }
    val tempApplicable = mutableListOf<Set<Int>>()
    for (i in 0 until nItems) {
        val parts = reader.readLine().split(" ")
        val count = parts[0].toInt()
        val indices = if (count > 0) parts.drop(1).map { it.toInt() - 1 }.toSet() else emptySet()
        tempApplicable.add(indices)
    }
    applicableCouponsPerItem = tempApplicable
    val discountPercentages = reader.readLine().split(" ").map { it.toInt() }
    discountMultipliers = discountPercentages.map { 1.0 - it / 100.0 }
    findBestCombination(0, mutableListOf(), k)
    writer.write("${bestCouponIndices.size}")
    writer.newLine()
    if (bestCouponIndices.isNotEmpty()) {
        writer.write(bestCouponIndices.map { it + 1 }.joinToString(" "))
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
