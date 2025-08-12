import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine() ?: ""
    if (s.isEmpty()) {
        writer.write("1")
        writer.newLine()
        writer.flush()
        return
    }
    var dpUp = 0
    var dpDown = Int.MAX_VALUE / 2
    for (ch in s) {
        val u = if (ch == '1' || ch == '3') 1 else 0
        val d = if (ch == '2' || ch == '3') 1 else 0
        val newUp = min(dpUp + u, dpDown + min(d + 1, u + 1))
        val newDown = min(dpDown + d, dpUp + min(u + 1, d + 1))
        dpUp = newUp
        dpDown = newDown
    }
    writer.write(dpDown.toString())
    writer.newLine()
    writer.flush()
}
