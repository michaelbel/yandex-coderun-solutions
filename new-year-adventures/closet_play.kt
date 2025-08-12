import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = PrintWriter(BufferedWriter(OutputStreamWriter(System.out)))

    val k = reader.readLine().toInt()
    val s = reader.readLine()
    val t = reader.readLine()

    if (k > s.length || k > t.length) {
        writer.println("NO")
        writer.flush()
        return
    }

    val ft = IntArray(26)
    for (ch in t) {
        ft[ch - 'a']++
    }

    val fw = IntArray(26)
    for (i in 0 until k) {
        fw[s[i] - 'a']++
    }

    fun windowIsValid(): Boolean {
        for (c in 0 until 26) {
            if (fw[c] > ft[c]) return false
        }
        return true
    }

    if (windowIsValid()) {
        writer.println("YES")
        writer.flush()
        return
    }

    for (i in k until s.length) {
        fw[s[i] - 'a']++
        fw[s[i - k] - 'a']--
        if (windowIsValid()) {
            writer.println("YES")
            writer.flush()
            return
        }
    }

    writer.println("NO")
    writer.flush()
}
