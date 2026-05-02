import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))

    val (n, q) = reader.readLine().split(' ').let { it[0].toInt() to it[1].toInt() }

    val sizeTree = 4 * n + 5
    val treeOps = Array<MutableList<Int>>(sizeTree) { mutableListOf() }

    fun addOp(node: Int, l: Int, r: Int, ql: Int, qr: Int, x: Int) {
        if (ql <= l && r <= qr) {
            treeOps[node].add(x)
        } else {
            val mid = (l + r) shr 1
            if (ql <= mid) addOp(node * 2, l, mid, ql, qr, x)
            if (qr >  mid) addOp(node * 2 + 1, mid + 1, r, ql, qr, x)
        }
    }

    repeat(q) {
        val parts = reader.readLine().split(' ')
        val l = parts[0].toInt()
        val r = parts[1].toInt()
        val x = parts[2].toInt()
        addOp(1, 1, n, l, r, x)
    }

    val sizeBits = n + 1
    val B = (sizeBits + 63) / 64
    val globalResult = LongArray(B) { 0L }

    fun bitsetOrShift(dp: LongArray, shift: Int) {
        val nWords = dp.size
        val wordShift = shift / 64
        if (wordShift >= nWords) return
        val bitShift = shift % 64
        if (bitShift == 0) {
            for (i in nWords - 1 downTo wordShift) {
                dp[i] = dp[i] or dp[i - wordShift]
            }
        } else {
            for (i in nWords - 1 downTo wordShift + 1) {
                dp[i] = dp[i] or
                    (dp[i - wordShift] shl bitShift) or
                    (dp[i - wordShift - 1].ushr(64 - bitShift))
            }
            dp[wordShift] = dp[wordShift] or (dp[0] shl bitShift)
        }
    }

    fun dfs(node: Int, l: Int, r: Int, dp: LongArray) {
        val cur = dp.clone()
        for (x in treeOps[node]) {
            bitsetOrShift(cur, x)
        }
        if (l == r) {
            for (i in 0 until B) {
                globalResult[i] = globalResult[i] or cur[i]
            }
        } else {
            val mid = (l + r) shr 1
            dfs(node * 2,     l,    mid, cur)
            dfs(node * 2 + 1, mid+1, r,   cur)
        }
    }

    val initDP = LongArray(B) { 0L }
    initDP[0] = 1L

    dfs(1, 1, n, initDP)

    val answer = mutableListOf<Int>()
    for (m in 1..n) {
        val word = m ushr 6
        val bit  = m and 63
        if ((globalResult[word].ushr(bit) and 1L) != 0L) {
            answer.add(m)
        }
    }

    writer.write("${answer.size}\n")
    if (answer.isNotEmpty()) {
        writer.write(answer.joinToString(" "))
        writer.newLine()
    }
    writer.flush()
    reader.close()
    writer.close()
}
