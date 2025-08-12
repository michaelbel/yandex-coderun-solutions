import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val t = reader.readLine().toInt()
    for (i in 0 until t) {
        val s1 = reader.readLine()
        val s2 = reader.readLine()
        if (s1.length != s2.length) {
            writer.write("NO")
            writer.newLine()
            continue
        }
        val mapping = IntArray(26) { -1 }
        val used = BooleanArray(26) { false }
        var possible = true
        for (j in s1.indices) {
            val c1 = s1[j] - 'a'
            val c2 = s2[j] - 'a'
            if (mapping[c1] == -1) {
                if (used[c2]) {
                    possible = false
                    break
                }
                mapping[c1] = c2
                used[c2] = true
            } else {
                if (mapping[c1] != c2) {
                    possible = false
                    break
                }
            }
        }
        writer.write(if (possible) "YES" else "NO")
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
