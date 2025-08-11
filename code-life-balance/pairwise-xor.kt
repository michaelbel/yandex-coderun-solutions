import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Arrays

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val t = reader.readLine().toInt()

    repeat(t) {
        val n = reader.readLine().toInt()
        val a = reader.readLine().split(" ").map { it.toInt() }.toIntArray()

        Arrays.sort(a)

        var minXor = Int.MAX_VALUE

        for (i in 1 until n) {
            minXor = minOf(minXor, a[i] xor a[i - 1])
        }

        writer.write("$minXor\n")
    }

    writer.flush()
    reader.close()
    writer.close()
}