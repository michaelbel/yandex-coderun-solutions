import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.PriorityQueue

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val T = firstLine[1].toLong()

    val friends = Array(n) { Pair(0L, 0L) }
    for (i in 0 until n) {
        val parts = reader.readLine().split(" ")
        val x = parts[0].toLong()
        val t = parts[1].toLong()
        friends[i] = Pair(x, t)
    }

    fun can(k: Int): Boolean {
        if (k == 0) return true
        if (k == 1) {
            for (i in 0 until n) {
                if (friends[i].first + friends[i].second <= T) return true
            }
            return false
        }
        var sum: Long = 0
        val heap = PriorityQueue<Long>(compareByDescending { it })
        for (i in 0 until n) {
            if (heap.size == k - 1) {
                val total = friends[i].first + friends[i].second + sum
                if (total <= T) return true
            }
            heap.add(friends[i].second)
            sum += friends[i].second
            if (heap.size > k - 1) {
                sum -= heap.poll()
            }
        }
        return false
    }

    var lo = 0
    var hi = n
    var ans = 0
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        if (can(mid)) {
            ans = mid
            lo = mid + 1
        } else {
            hi = mid - 1
        }
    }

    writer.write(ans.toString())
    writer.flush()
    reader.close()
    writer.close()
}
