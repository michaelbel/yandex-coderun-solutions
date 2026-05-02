import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val a = Array(3) { LongArray(n + 1) }
    repeat(3) { i ->
        val st = StringTokenizer(reader.readLine())
        for (j in 1..n) {
            a[i][j] = a[i][j - 1] + st.nextToken().toLong()
        }
    }

    var answer = Long.MAX_VALUE
    val perms = listOf(
        Triple(0, 1, 2),
        Triple(0, 2, 1),
        Triple(1, 0, 2),
        Triple(1, 2, 0),
        Triple(2, 0, 1),
        Triple(2, 1, 0)
    )

    for ((p1, p2, p3) in perms) {
        val pref1 = a[p1]
        val pref2 = a[p2]
        val pref3 = a[p3]
        var mnF = pref1[1] - pref2[1]
        var bestMid = Long.MAX_VALUE

        for (j in 2 until n) {
            val candidate = mnF + (pref2[j] - pref3[j])
            if (candidate < bestMid) bestMid = candidate
            val fAtJ = pref1[j] - pref2[j]
            if (fAtJ < mnF) mnF = fAtJ
        }
        val total = bestMid + pref3[n]
        if (total < answer) answer = total
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
