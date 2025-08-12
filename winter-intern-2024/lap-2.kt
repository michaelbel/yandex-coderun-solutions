import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger
import kotlin.math.min

private class FenwickTree(size: Int) {
    private val tree = LongArray(size + 1)
    private val internalSize = size

    fun update(idx: Int, delta: Long) {
        if (idx <= 0 || idx > internalSize) return
        var i = idx
        while (i <= internalSize) {
            tree[i] += delta
            i += i and -i
        }
    }

    fun query(idx: Int): Long {
        if (idx <= 0) return 0L
        var i = minOf(idx, internalSize)
        var sum = 0L
        while (i > 0) {
            sum += tree[i]
            i -= i and -i
        }
        return sum
    }
}

private data class CarInfo(
    val speed: Int,
    val laps: Long,
    val pos: Int
)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ")
    val n = firstLine[0].toInt()
    val t = firstLine[1].toLong()
    val s = firstLine[2].toInt()

    val speeds = reader.readLine().trim().split(' ').map { it.toInt() }

    val carInfos = List(n) { index ->
        val v = speeds[index].toLong()
        val vt = v * t
        val laps = vt / s
        val sLong = s.toLong()
        val pos = ((vt % sLong + sLong) % sLong).toInt()
        CarInfo(speed = speeds[index], laps = laps, pos = pos)
    }

    val sortedCars = carInfos.sortedBy { it.speed }

    var sum1BI = BigInteger.ZERO
    val nLong = n.toLong()
    val nBI = nLong.toBigInteger()
    val oneBI = BigInteger.ONE
    val twoBI = BigInteger.TWO

    for (k_idx in 0 until n) {
        val kLong = k_idx + 1L
        val kBI = kLong.toBigInteger()
        val i_k_long = sortedCars[k_idx].laps
        if (i_k_long != 0L) {
            val i_k_BI = BigInteger.valueOf(i_k_long)
            val termMultiplier = twoBI.multiply(kBI).subtract(nBI).subtract(oneBI)
            sum1BI = sum1BI.add(i_k_BI.multiply(termMultiplier))
        }
    }

    val bit = FenwickTree(s)
    var inv_count = 0L

    for (k_idx in 0 until n) {
        val currentCar = sortedCars[k_idx]
        val position = currentCar.pos
        val bitIndex = position + 1
        val countLessThanPos = bit.query(bitIndex - 1)
        val countGreaterEqualPos = k_idx.toLong() - countLessThanPos
        inv_count += countGreaterEqualPos
        bit.update(bitIndex, 1L)
    }

    val inv_count_BI = BigInteger.valueOf(inv_count)
    val totalOvertakesBI = sum1BI.subtract(inv_count_BI)

    writer.write(totalOvertakesBI.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
