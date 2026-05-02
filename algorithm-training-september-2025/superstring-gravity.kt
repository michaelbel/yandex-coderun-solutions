import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    fun nextInt(): Int = next().toInt()
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val a = IntArray(n + 1)
    var maxVal = 0

    for (i in 1..n) {
        val v = fs.nextInt()
        a[i] = v
        if (v > maxVal) maxVal = v
    }

    val m = fs.nextInt()
    val b = IntArray(m + 1)
    for (j in 1..m) {
        val v = fs.nextInt()
        b[j] = v
        if (v > maxVal) maxVal = v
    }

    val size = maxVal + 2

    // Частота и сумма значений для A и B
    val cntA = LongArray(size)
    val sumAVal = LongArray(size)
    val cntB = LongArray(size)
    val sumBVal = LongArray(size)

    for (i in 1..n) {
        val v = a[i]
        cntA[v]++
        sumAVal[v] += v.toLong()
    }
    for (j in 1..m) {
        val v = b[j]
        cntB[v]++
        sumBVal[v] += v.toLong()
    }

    // Префиксные суммы по значениям
    val prefCntA = LongArray(size)
    val prefSumA = LongArray(size)
    val prefCntB = LongArray(size)
    val prefSumB = LongArray(size)

    for (v in 1..maxVal) {
        prefCntA[v] = prefCntA[v - 1] + cntA[v]
        prefSumA[v] = prefSumA[v - 1] + sumAVal[v]
        prefCntB[v] = prefCntB[v - 1] + cntB[v]
        prefSumB[v] = prefSumB[v - 1] + sumBVal[v]
    }

    val totalCntA = prefCntA[maxVal]
    val totalSumA = prefSumA[maxVal]
    val totalCntB = prefCntB[maxVal]
    val totalSumB = prefSumB[maxVal]

    // C_B[v] = sum_j |v - b_j|
    val CB = LongArray(size)
    // D_A[v] = sum_i |v - a_i|
    val DA = LongArray(size)

    for (v in 1..maxVal) {
        val vv = v.toLong()

        // Для B
        val leftCntB = prefCntB[v]
        val leftSumB = prefSumB[v]
        val rightCntB = totalCntB - leftCntB
        val rightSumB = totalSumB - leftSumB
        CB[v] = vv * leftCntB - leftSumB + rightSumB - vv * rightCntB

        // Для A
        val leftCntA = prefCntA[v]
        val leftSumA = prefSumA[v]
        val rightCntA = totalCntA - leftCntA
        val rightSumA = totalSumA - leftSumA
        DA[v] = vv * leftCntA - leftSumA + rightSumA - vv * rightCntA
    }

    // S = sum_{i,j} (i - j) * |a_i - b_j|
    //   = sum_i i * sum_j |a_i - b_j| - sum_j j * sum_i |a_i - b_j|
    var sumA = 0L
    for (i in 1..n) {
        val v = a[i]
        sumA += i.toLong() * CB[v]
    }

    var sumB = 0L
    for (j in 1..m) {
        val v = b[j]
        sumB += j.toLong() * DA[v]
    }

    val result = sumA - sumB
    writer.write(result.toString())
    writer.newLine()
    writer.flush()
}
