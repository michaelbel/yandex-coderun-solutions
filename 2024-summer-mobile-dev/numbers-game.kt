import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val line = reader.readLine()
    if (line.isEmpty()) {
        writer.write("0")
        writer.flush()
        return
    }
    val st = StringTokenizer(line)
    val n = st.countTokens()
    val values = IntArray(n)
    val counts = IntArray(n)
    var top = 0
    var removed = 0
    while (st.hasMoreTokens()) {
        val x = st.nextToken().toInt()
        if (top > 0 && values[top - 1] == x) {
            counts[top - 1]++
        } else {
            if (top > 0 && counts[top - 1] >= 3) {
                removed += counts[top - 1]
                top--
                if (top > 0 && values[top - 1] == x) {
                    counts[top - 1]++
                } else {
                    values[top] = x
                    counts[top] = 1
                    top++
                }
            } else {
                values[top] = x
                counts[top] = 1
                top++
            }
        }
    }
    while (top > 0 && counts[top - 1] >= 3) {
        removed += counts[top - 1]
        top--
    }
    writer.write(removed.toString())
    writer.flush()
}
