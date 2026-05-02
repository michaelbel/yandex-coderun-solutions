import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val t = reader.readLine()!!
    val s = reader.readLine()!!
    val n = t.length
    val m = s.length

    if (m > n) {
        writer.write("0")
        writer.newLine()
        writer.flush()
        return
    }

    val aS = IntArray(m)
    val rawA = IntArray(m)
    val lastOccPattern = IntArray(128) { -1 }
    val INF_SP = m + 1
    for (j in 0 until m) {
        val c = s[j].code
        val last = lastOccPattern[c]
        rawA[j] = if (last < 0) INF_SP else j - last
        aS[j] = if (last < 0) 0 else j - last
        lastOccPattern[c] = j
    }

    val rawT = IntArray(n)
    val lastOccT = IntArray(128) { -1 }
    val INF_T = n + 1
    for (k in 0 until n) {
        val c = t[k].code
        val last = lastOccT[c]
        rawT[k] = if (last < 0) INF_T else k - last
        lastOccT[c] = k
    }

    val pi = IntArray(m)
    pi[0] = 0
    for (i in 1 until m) {
        var len = pi[i - 1]
        while (len > 0) {
            val raw = rawA[i]
            val prevBound = if (raw > len) 0 else raw
            if (prevBound == aS[len]) break
            len = pi[len - 1]
        }
        val raw = rawA[i]
        val prevBound = if (raw > len) 0 else raw
        pi[i] = if (prevBound == aS[len]) len + 1 else 0
    }

    val result = mutableListOf<Int>()
    var len = 0
    for (k in 0 until n) {
        while (len > 0) {
            val raw = rawT[k]
            val prevBound = if (raw > len) 0 else raw
            if (prevBound == aS[len]) break
            len = pi[len - 1]
        }
        val raw = rawT[k]
        val prevBound = if (raw > len) 0 else raw
        if (prevBound == aS[len]) len++
        if (len == m) {
            result.add(k - m + 2)
            len = pi[len - 1]
        }
    }

    writer.write("${result.size}")
    writer.newLine()
    if (result.isNotEmpty()) {
        val sb = StringBuilder()
        for (pos in result) {
            sb.append(pos).append(' ')
        }
        sb.setLength(sb.length - 1)
        writer.write(sb.toString())
        writer.newLine()
    }
    writer.flush()
}
