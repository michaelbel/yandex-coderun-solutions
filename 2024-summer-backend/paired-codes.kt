import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val freq = IntArray(1 shl 10)

    val st = StringTokenizer(reader.readLine())
    repeat(n) {
        val token = st.nextToken()
        var mask = 0
        for (ch in token) {
            mask = mask or (1 shl (ch - '0'))
        }
        freq[mask]++
    }

    val totalPairs = n.toLong() * (n - 1) / 2

    var disjointPairs = 0L
    val mSize = freq.size
    for (m1 in 0 until mSize) {
        val c1 = freq[m1]
        if (c1 == 0) continue
        if (m1 == 0 && c1 > 1) {
            disjointPairs += c1.toLong() * (c1 - 1) / 2
        }
        for (m2 in m1 + 1 until mSize) {
            val c2 = freq[m2]
            if (c2 == 0) continue
            if (m1 and m2 == 0) {
                disjointPairs += c1.toLong() * c2
            }
        }
    }

    val result = totalPairs - disjointPairs
    writer.write(result.toString())
    writer.newLine()

    writer.flush()
    reader.close()
    writer.close()
}
