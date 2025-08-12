import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val header = StringTokenizer(reader.readLine())
    val k = header.nextToken().toInt()
    val t = header.nextToken().toLong()
    val intervals = ArrayList<Pair<Long, Long>>(k)
    repeat(k) {
        val tok = StringTokenizer(reader.readLine())
        val start = tok.nextToken().toLong()
        val end = tok.nextToken().toLong()
        val brightness = tok.nextToken().toLong()
        val requiredSpan = end - start + 1
        if (brightness >= requiredSpan) {
            val L = end
            val R = minOf(start + brightness - 1, t)
            intervals.add(Pair(L, R))
        }
    }
    reader.close()
    intervals.sortWith(compareBy<Pair<Long, Long>>({ it.first }, { -it.second }))
    var coveredUpTo = 0L
    for ((L, R) in intervals) {
        if (L > coveredUpTo + 1) break
        if (R > coveredUpTo) {
            coveredUpTo = R
            if (coveredUpTo >= t) break
        }
    }
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    writer.write(if (coveredUpTo >= t) "Yes" else "No")
    writer.flush()
    writer.close()
}
