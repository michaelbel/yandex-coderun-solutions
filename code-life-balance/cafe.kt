import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val N = reader.readLine().toInt()
    val costs = IntArray(N + 1)
    for (i in 1..N) {
        costs[i] = reader.readLine().toInt()
    }

    if (N == 0) {
        writer.write("0\n")
        writer.write("0 0\n")
        writer.close()
        return
    }

    val INF = 10_000_000
    val dp = Array(N + 1) { IntArray(N + 1) { INF } }
    val prev = Array(N + 1) { Array<Pair<Int, Boolean>?>(N + 1) { null } }

    dp[0][0] = 0

    for (day in 1..N) {
        for (kOld in 0..N) {
            val oldCost = dp[day - 1][kOld]
            if (oldCost == INF) continue

            val costPay = oldCost + costs[day]
            val kNewPay = if (costs[day] > 100) kOld + 1 else kOld

            if (kNewPay <= N && costPay < dp[day][kNewPay]) {
                dp[day][kNewPay] = costPay
                prev[day][kNewPay] = Pair(kOld, false)
            }

            if (kOld > 0) {
                val costUseCoupon = oldCost
                val kNewCoupon = kOld - 1
                if (costUseCoupon < dp[day][kNewCoupon]) {
                    dp[day][kNewCoupon] = costUseCoupon
                    prev[day][kNewCoupon] = Pair(kOld, true)
                }
            }
        }
    }

    var minCost = INF
    var bestK = 0
    for (k in 0..N) {
        if (dp[N][k] < minCost) {
            minCost = dp[N][k]
            bestK = k
        } else if (dp[N][k] == minCost && k > bestK) {
            bestK = k
        }
    }

    val usedDays = mutableListOf<Int>()
    var curK = bestK
    var i = N

    while (i > 0) {
        val (prevK, usedCoupon) = prev[i][curK]!!
        if (usedCoupon) {
            usedDays.add(i)
        }
        curK = prevK
        i--
    }

    usedDays.reverse()

    val K2 = usedDays.size
    val K1 = bestK

    writer.write("$minCost\n")
    writer.write("$K1 $K2\n")
    for (day in usedDays) {
        writer.write("$day\n")
    }

    writer.close()
}