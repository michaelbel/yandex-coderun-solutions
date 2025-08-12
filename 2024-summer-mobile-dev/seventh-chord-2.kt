import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.floor
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val tokens = reader.readLine().split(" ")
    val a = IntArray(n)
    for (i in 0 until n) {
        a[i] = tokens[i].toInt()
    }
    var maxA = 0
    for (value in a) {
        if (value > maxA) maxA = value
    }
    val K_max = if (maxA > 0) floor(sqrt(maxA.toDouble())).toInt() else 0
    var result = 0L
    for (k in 1..K_max) {
        val thresh = k * k
        var sumWindow = 0
        var r = 0
        for (l in 0 until n) {
            while (r < n && sumWindow < k) {
                if (a[r] >= thresh) {
                    sumWindow++
                }
                r++
            }
            if (sumWindow >= k) {
                result += (n - r + 1)
            } else {
                break
            }
            if (a[l] >= thresh) {
                sumWindow--
            }
        }
    }
    writer.write(result.toString())
    writer.flush()
    writer.close()
    reader.close()
}
