import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val a = n.toString()
    var sum = 0
    for (ch in a) {
        sum += (ch - '0')
    }
    fun gcd(x: Int, y: Int): Int {
        var a = x
        var b = y
        while (b != 0) {
            val t = a % b
            a = b
            b = t
        }
        return a
    }
    val g = gcd(sum, n)
    val t = n / g

    writer.write("I got it")
    writer.newLine()
    for (i in 1..t) {
        writer.write(a)
    }
    writer.newLine()

    writer.flush()
    writer.close()
    reader.close()
}
