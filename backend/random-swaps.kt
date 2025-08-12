import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.pow
import java.util.Locale

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val nStr = reader.readLine()
    val k = reader.readLine().toInt()
    val l = nStr.length
    val counts = IntArray(10)
    var sumDigits = 0
    var dLast = 0
    for (i in nStr.indices) {
        val digit = nStr[i].digitToInt()
        counts[digit]++
        sumDigits += digit
        if (i == l - 1) {
            dLast = digit
        }
    }
    val isSumDivisibleBy3 = (sumDigits % 3 == 0)
    val a: Double = when (l) {
        2 -> -1.0
        3 -> 0.0
        else -> (l - 3.0) / (l - 1.0)
    }
    val ak: Double = when {
        k == 0 -> 1.0
        l == 3 -> 0.0
        l == 2 -> if (k % 2 == 0) 1.0 else -1.0
        else -> a.pow(k.toDouble())
    }
    var totalProb = 0.0
    for (d in 1..9) {
        var isTarget = false
        if (d == 5) {
            isTarget = true
        }
        if (isSumDivisibleBy3 && (d % 2 == 0)) {
            isTarget = true
        }
        if (isTarget) {
            val countD = counts[d]
            if (countD == 0) continue
            val qStar = countD.toDouble() / l.toDouble()
            val probDAtLast: Double = if (d == dLast) {
                val initialQ = 1.0
                qStar + (initialQ - qStar) * ak
            } else {
                val initialQ = 0.0
                qStar + (initialQ - qStar) * ak
            }
            totalProb += probDAtLast
        }
    }
    writer.write(String.format(Locale.US, "%.15f", totalProb))
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
