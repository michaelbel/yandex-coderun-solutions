import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayList
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (n, k, w) = reader.readLine().split(" ").map { it.toInt() }
    val weekCosts = ArrayList<Int>()
    for (i in 0 until k) {
        val (ci, wi) = reader.readLine().split(" ").map { it.toInt() }
        repeat(wi) {
            weekCosts.add(ci)
        }
    }
    weekCosts.sortDescending()
    val totalSlots = n.toLong() * w.toLong()
    var maxProfit: Long = 0
    val weeksToConsider = minOf(totalSlots, weekCosts.size.toLong()).toInt()
    for (i in 0 until weeksToConsider) {
        maxProfit += weekCosts[i]
    }
    writer.write(maxProfit.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
