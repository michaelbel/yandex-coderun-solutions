import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val times = IntArray(n)
    repeat(n) { i ->
        val s = reader.readLine()
        val h = (s[0] - '0') * 10 + (s[1] - '0')
        val m = (s[3] - '0') * 10 + (s[4] - '0')
        val sec = (s[6] - '0') * 10 + (s[7] - '0')
        times[i] = h * 3600 + m * 60 + sec
    }
    val segStarts = mutableListOf<Int>()
    val segEnds = mutableListOf<Int>()
    segStarts += 0
    for (i in 1 until n) {
        if (times[i] <= times[i - 1]) segStarts += i
    }
    for (k in 0 until segStarts.size - 1) {
        segEnds += segStarts[k + 1] - 1
    }
    segEnds += n - 1
    val d = segStarts.size
    val INF = Int.MAX_VALUE / 4
    var prev1Cost = 1
    var prev1Amax = times[segEnds[0]]
    var prev2Cost = INF
    var prev2Amax = 0
    for (j in 1 until d) {
        val st = segStarts[j]
        val en = segEnds[j]
        var dp1Cost = INF
        var dp1Amax = -1
        if (prev1Amax >= times[st]) {
            val idx = lastLeq(times, st, en, prev1Amax)
            if (idx >= st) {
                val cost = prev1Cost + 1
                val amax = times[idx]
                if (cost < dp1Cost) {
                    dp1Cost = cost
                    dp1Amax = amax
                } else if (cost == dp1Cost && amax > dp1Amax) {
                    dp1Amax = amax
                }
            }
        }
        if (prev2Cost < INF && prev2Amax >= times[st]) {
            val idx = lastLeq(times, st, en, prev2Amax)
            if (idx >= st) {
                val cost = prev2Cost + 1
                val amax = times[idx]
                if (cost < dp1Cost) {
                    dp1Cost = cost
                    dp1Amax = amax
                } else if (cost == dp1Cost && amax > dp1Amax) {
                    dp1Amax = amax
                }
            }
        }
        var dp2Cost = INF
        if (prev1Amax >= times[st]) {
            val cost = prev1Cost + 2
            if (cost < dp2Cost) dp2Cost = cost
        }
        if (prev2Cost < INF && prev2Amax >= times[st]) {
            val cost = prev2Cost + 2
            if (cost < dp2Cost) dp2Cost = cost
        }
        prev1Cost = dp1Cost
        prev1Amax = dp1Amax
        prev2Cost = dp2Cost
        prev2Amax = times[en]
    }
    val picks = prev1Cost
    writer.write((n - picks).toString())
    writer.newLine()
    writer.flush()
}

fun lastLeq(arr: IntArray, lo: Int, hi: Int, value: Int): Int {
    var left = lo
    var right = hi
    var ans = lo - 1
    while (left <= right) {
        val mid = (left + right) ushr 1
        if (arr[mid] <= value) {
            ans = mid
            left = mid + 1
        } else {
            right = mid - 1
        }
    }
    return ans
}
