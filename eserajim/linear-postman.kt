import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val (a, b) = reader.readLine().split(' ').let { it[0].toInt() to it[1].toInt() }
    val x = IntArray(n)
    var cur = 0
    fun nextRand24(): Int {
        cur = cur * a + b
        return cur ushr 8
    }
    fun nextRand32(): Int {
        val u = nextRand24()
        val v = nextRand24()
        return (u shl 8) xor v
    }
    for (i in 0 until n) {
        x[i] = nextRand32()
    }
    val k = n ushr 1
    var left = 0
    var right = n - 1
    while (left < right) {
        val pivot = x[left + ((right - left) ushr 1)]
        var i = left
        var j = right
        while (i <= j) {
            while (java.lang.Integer.compareUnsigned(x[i], pivot) < 0) i++
            while (java.lang.Integer.compareUnsigned(x[j], pivot) > 0) j--
            if (i <= j) {
                val tmp = x[i]; x[i] = x[j]; x[j] = tmp
                i++; j--
            }
        }
        when {
            k <= j -> right = j
            k >= i -> left = i
            else -> break
        }
    }
    val med = x[k].toLong() and 0xFFFFFFFFL
    var sum = 0L
    for (v in x) {
        val uv = v.toLong() and 0xFFFFFFFFL
        sum += if (uv >= med) uv - med else med - uv
    }
    writer.write(sum.toString())
    writer.flush()
}
