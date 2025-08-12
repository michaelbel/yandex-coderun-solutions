import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val n = reader.readLine().toInt()
    val tokens = reader.readLine().split(" ")
    val arr = IntArray(n) { tokens[it].toInt() }
    var S: Long = 0
    var maxVal = 0
    for (v in arr) {
        S += v.toLong()
        if (v > maxVal) maxVal = v
    }
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    if (n == 2) {
        val ans = (10000L / arr[0]) + (10000L / arr[1])
        writer.write(ans.toString())
        writer.flush()
        reader.close()
        writer.close()
        return
    }
    if (S <= 20000) {
        val SInt = S.toInt()
        val dp = BooleanArray(SInt + 1)
        dp[0] = true
        for (v in arr) {
            for (j in SInt downTo v) {
                if (dp[j - v]) {
                    dp[j] = true
                }
            }
        }
        var best = Long.MAX_VALUE
        for (s in 1 until SInt) {
            if (dp[s]) {
                val cand = (10000L / s) + (10000L / (SInt - s))
                if (cand < best) best = cand
            }
        }
        writer.write(best.toString())
        writer.flush()
        reader.close()
        writer.close()
        return
    } else {
        if (n == 3) {
            var best = Long.MAX_VALUE
            for (i in 0 until 3) {
                val part = arr[i].toLong()
                val other = S - part
                if (part == 0L || other == 0L) continue
                val cand = (10000L / part) + (10000L / other)
                if (cand < best) best = cand
            }
            writer.write(best.toString())
            writer.flush()
            reader.close()
            writer.close()
            return
        } else {
            val half = (S / 2).toInt()
            val L = if (half < 10000 + maxVal) half else (10000 + maxVal)
            val size = (L + 64) / 64
            val dp = LongArray(size)
            dp[0] = 1L
            for (v in arr) {
                if (v > L) continue
                val shifted = shiftLeft(dp, v, L)
                for (i in 0 until size) {
                    dp[i] = dp[i] or shifted[i]
                }
            }
            val upperBound = if (S - 10001 < L.toLong()) (S - 10001).toInt() else L
            var exists = false
            for (s in 10001..upperBound) {
                if (testBit(dp, s)) {
                    exists = true
                    break
                }
            }
            if (exists) {
                writer.write("0")
                writer.flush()
                reader.close()
                writer.close()
                return
            } else {
                val limit = if (10000 < L) 10000 else L
                var m0 = 0
                for (s in 1..limit) {
                    if (testBit(dp, s)) m0 = s
                }
                val ans = 10000L / m0
                writer.write(ans.toString())
                writer.flush()
                reader.close()
                writer.close()
                return
            }
        }
    }
}

fun testBit(dp: LongArray, pos: Int): Boolean {
    val index = pos / 64
    val bit = pos % 64
    return (dp[index] and (1L shl bit)) != 0L
}

fun shiftLeft(dp: LongArray, shift: Int, L: Int): LongArray {
    val n = dp.size
    val res = LongArray(n)
    val wordShift = shift / 64
    val bitShift = shift % 64
    for (i in 0 until n) {
        val srcIndex = i - wordShift
        var x = if (srcIndex in 0 until n) dp[srcIndex] shl bitShift else 0L
        if (bitShift != 0 && srcIndex - 1 in 0 until n) {
            x = x or (dp[srcIndex - 1] ushr (64 - bitShift))
        }
        res[i] = x
    }
    val totalBits = n * 64
    val excess = totalBits - (L + 1)
    if (excess > 0) {
        res[n - 1] = res[n - 1] and ((1L shl (64 - excess)) - 1)
    }
    return res
}
