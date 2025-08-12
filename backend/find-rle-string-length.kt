import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class Run(val letter: Char, val count: Long, val start: Long, val end: Long)

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine()
    val runs = ArrayList<Run>()
    var i = 0
    var pos = 1L
    while (i < s.length) {
        var cnt = 1L
        if (s[i].isDigit()) {
            var num = 0L
            while (i < s.length && s[i].isDigit()) {
                num = num * 10 + (s[i] - '0')
                i++
            }
            cnt = num
        }
        val letter = s[i]
        i++
        val start = pos
        val end = pos + cnt - 1
        runs.add(Run(letter, cnt, start, end))
        pos = end + 1
    }
    val nRuns = runs.size
    val ends = LongArray(nRuns)
    val starts = LongArray(nRuns)
    for (j in 0 until nRuns) {
        ends[j] = runs[j].end
        starts[j] = runs[j].start
    }
    fun digits(x: Long): Int {
        var cnt = 0
        var y = x
        while (y > 0) {
            cnt++
            y /= 10
        }
        return cnt
    }
    fun tokenLen(cnt: Long): Int {
        return if (cnt == 1L) 1 else digits(cnt) + 1
    }
    val tokenLens = IntArray(nRuns)
    for (j in 0 until nRuns) tokenLens[j] = tokenLen(runs[j].count)
    val pre = IntArray(nRuns + 1)
    for (j in 0 until nRuns) pre[j + 1] = pre[j] + tokenLens[j]
    fun findRun(posInT: Long): Int {
        var lo = 0
        var hi = nRuns - 1
        while (lo < hi) {
            val mid = (lo + hi) / 2
            if (ends[mid] >= posInT) hi = mid else lo = mid + 1
        }
        return lo
    }
    val q = reader.readLine().toInt()
    val output = StringBuilder()
    repeat(q) {
        val parts = reader.readLine().split(" ")
        val l = parts[0].toLong()
        val r = parts[1].toLong()
        val Lidx = findRun(l)
        val Ridx = findRun(r)
        val ans = if (Lidx == Ridx) {
            val segLen = r - l + 1
            if (segLen == 1L) 1 else digits(segLen) + 1
        } else {
            val partA = runs[Lidx].end - l + 1
            val tokenA = if (partA == 1L) 1 else digits(partA) + 1
            val partC = r - runs[Ridx].start + 1
            val tokenC = if (partC == 1L) 1 else digits(partC) + 1
            val midSum = if (Ridx - Lidx - 1 > 0) pre[Ridx] - pre[Lidx + 1] else 0
            tokenA + tokenC + midSum
        }
        output.append(ans).append("\n")
    }
    writer.write(output.toString())
    writer.flush()
    writer.close()
    reader.close()
}
