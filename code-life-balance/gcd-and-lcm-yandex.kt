import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.sqrt

private fun gcd(a: Long, b: Long): Long {
    var x = a
    var y = b
    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (x, y) = reader.readLine().split(" ").map(String::toLong)

    if (y % x != 0L) {
        writer.write("0")
        writer.newLine()
        writer.flush()
        reader.close()
        writer.close()
        return
    }

    val k = y / x
    var count = 0L

    for (p in 1..sqrt(k.toDouble()).toLong()) {
        if (k % p == 0L) {
            val q = k / p
            if (gcd(p, q) == 1L) {
                count += if (p == q) 1 else 2
            }
        }
    }

    writer.write(count.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
