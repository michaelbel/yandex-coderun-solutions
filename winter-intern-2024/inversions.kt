import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Math

fun gcd(a: Long, b: Long): Long {
    var n1 = Math.abs(a)
    var n2 = Math.abs(b)
    while (n2 != 0L) {
        val temp = n1 % n2
        n1 = n2
        n2 = temp
    }
    return n1
}

class FenwickTree(size: Int) {
    private val tree = LongArray(size + 1)
    private val internalSize = size + 1
    fun update(idx: Int, delta: Long) {
        var i = idx
        while (i < internalSize) {
            tree[i] += delta
            i += i and -i
        }
    }
    fun query(idx: Int): Long {
        var sum = 0L
        var i = idx
        while (i > 0) {
            sum += tree[i]
            i -= i and -i
        }
        return sum
    }
    fun queryTotal(): Long {
        return query(internalSize - 1)
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val p = reader.readLine().split(" ").map { it.toInt() }.toIntArray()

    val al = LongArray(n + 1)
    val cl = LongArray(n + 1)
    val br = LongArray(n + 1)
    val dr = LongArray(n + 1)

    var initialInversions: Long = 0L
    val ftLeft = FenwickTree(n)
    for (k in 1..n) {
        val pk = p[k - 1]
        al[k] = ftLeft.query(pk - 1)
        cl[k] = ftLeft.queryTotal() - ftLeft.query(pk)
        initialInversions += cl[k]
        ftLeft.update(pk, 1L)
    }

    val ftRight = FenwickTree(n)
    for (k in n downTo 1) {
        val pk = p[k - 1]
        dr[k] = ftRight.query(pk - 1)
        br[k] = ftRight.queryTotal() - ftRight.query(pk)
        ftRight.update(pk, 1L)
    }

    var t2Sum: Long = 0L
    for (k in 1..n) {
        t2Sum += br[k] * al[k] - dr[k] * cl[k]
    }
    val t2: Long = 2L * t2Sum

    val nLong = n.toLong()
    val nSwaps: Long = nLong * (nLong - 1L) / 2L
    val num: Long = initialInversions * nSwaps + nSwaps - 2L * initialInversions + t2
    val den: Long = nSwaps

    if (num == 0L) {
        writer.write("0/1")
    } else {
        val common = gcd(num, den)
        val finalNum = num / common
        val finalDen = den / common
        if (finalDen < 0) {
            writer.write("${-finalNum}/${-finalDen}")
        } else {
            writer.write("$finalNum/$finalDen")
        }
    }

    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
