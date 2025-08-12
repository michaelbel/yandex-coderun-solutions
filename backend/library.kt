import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (kInput, mInput, dInput) = reader.readLine().split(" ")
    val k = kInput.toLong()
    val m = mInput.toLong()
    val d = dInput.toInt()
    val P = LongArray(7)
    for (r in 0 until 7) {
        var count = 0L
        for (j in 0..r) {
            val dayOfWeek = ((d - 1 + j) % 7) + 1
            if (dayOfWeek <= 5) count++
        }
        P[r] = count
    }
    fun fCandidate(w: Long, r: Int): Long {
        val i = 7 * w + r + 1
        return 2 * m + 2 * k * (5 * w + P[r]) - i * (i + 1)
    }
    fun feasible(D: Long): Boolean {
        var overallMin = Long.MAX_VALUE
        for (r in 0 until 7) {
            if (r + 1 > D) continue
            val wMax = (D - (r + 1)) / 7
            val cand1 = fCandidate(0L, r)
            val cand2 = fCandidate(wMax, r)
            val localMin = if (cand1 < cand2) cand1 else cand2
            if (localMin < overallMin) overallMin = localMin
        }
        return overallMin >= 0
    }
    var low = 0L
    var high = 2100000000L
    var ans = 0L
    while (low <= high) {
        val mid = (low + high) / 2
        if (feasible(mid)) {
            ans = mid
            low = mid + 1
        } else {
            high = mid - 1
        }
    }
    writer.write("$ans")
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
