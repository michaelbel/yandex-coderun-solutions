import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val text = reader.readLine() ?: ""
    val n = text.length
    val dictCount = reader.readLine().trim().toInt()

    val dict = Array(dictCount) { "" }
    for (i in 0 until dictCount) {
        dict[i] = reader.readLine()
    }

    val dp = BooleanArray(n + 1)
    val prevPos = IntArray(n + 1) { -1 }
    val prevWordIndex = IntArray(n + 1) { -1 }

    dp[0] = true

    for (i in 0 until n) {
        if (!dp[i]) continue
        for (wIndex in 0 until dictCount) {
            val w = dict[wIndex]
            val len = w.length
            val j = i + len
            if (j > n) continue
            if (dp[j]) continue
            var ok = true
            var k = 0
            while (k < len) {
                if (text[i + k] != w[k]) {
                    ok = false
                    break
                }
                k++
            }
            if (ok) {
                dp[j] = true
                prevPos[j] = i
                prevWordIndex[j] = wIndex
            }
        }
    }

    // Гарантируется, что разбиение существует, значит dp[n] == true
    val resultWords = ArrayList<String>()
    var pos = n
    while (pos > 0) {
        val wIndex = prevWordIndex[pos]
        val w = dict[wIndex]
        resultWords.add(w)
        pos = prevPos[pos]
    }
    resultWords.reverse()

    val sb = StringBuilder()
    for (w in resultWords) {
        sb.append(w).append(' ')
    }

    writer.write(sb.toString())
    writer.newLine()
    writer.flush()
}
