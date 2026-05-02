import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val line = reader.readLine() ?: run {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }

    val st = StringTokenizer(line)
    val a = st.nextToken().toLong()
    val b = st.nextToken().toLong()
    val s = st.nextToken().toLong()

    // (L - a) * (L - b) = S
    // L^2 - (a + b)L + (a*b - S) = 0
    // D = (a - b)^2 + 4S
    val diff = a - b
    val d = diff * diff + 4L * s

    // integer sqrt of d
    var t = kotlin.math.sqrt(d.toDouble()).toLong()
    while ((t + 1) * (t + 1) <= d) t++
    while (t * t > d) t--

    if (t * t != d) {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }

    val sum = a + b
    var answer = -1L

    // Larger root should be the valid one (if any)
    val num = sum + t
    if (num % 2L == 0L) {
        val L = num / 2L
        if (L >= a && L >= b) {
            val left = (L - a) * (L - b)
            if (left == s) {
                answer = L
            }
        }
    }

    if (answer == -1L) {
        writer.write("-1")
    } else {
        writer.write(answer.toString())
    }
    writer.newLine()
    writer.flush()
}
