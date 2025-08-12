import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

class FenwickTree(size: Int) {
    val tree = LongArray(size + 1)
    val maxSize = size
    fun update(idx: Int, delta: Long) {
        var i = idx
        while (i <= maxSize) {
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
    fun findKth(k: Long): Int {
        var pos = 0
        var currentSum = 0L
        var bitMask = 1
        while (bitMask * 2 <= maxSize) bitMask *= 2
        while (bitMask > 0) {
            val nextPos = pos + bitMask
            if (nextPos <= maxSize && currentSum + tree[nextPos] < k) {
                currentSum += tree[nextPos]
                pos = nextPos
            }
            bitMask /= 2
        }
        return pos + 1
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val firstLine = StringTokenizer(reader.readLine())
    val n = firstLine.nextToken().toInt()
    val m = firstLine.nextToken().toInt()
    val type = firstLine.nextToken().toInt()
    val text = IntArray(n)
    val textLine = StringTokenizer(reader.readLine())
    for (i in 0 until n) {
        text[i] = textLine.nextToken().toInt()
    }
    val maxPosValue = n + m
    val bit = FenwickTree(maxPosValue)
    val pos = IntArray(m + 1)
    val symbolAtPos = IntArray(maxPosValue + 1)
    var currentTime = m + 1
    for (i in 1..m) {
        val initialPos = m - i + 1
        pos[i] = initialPos
        symbolAtPos[initialPos] = i
        bit.update(initialPos, 1)
    }
    val result = IntArray(n)
    for (i in 0 until n) {
        if (type == 1) {
            val x = text[i]
            val currentPos = pos[x]
            val totalActive = bit.query(maxPosValue)
            val countBefore = bit.query(currentPos - 1)
            val rank = totalActive - countBefore
            result[i] = rank.toInt()
            bit.update(currentPos, -1)
            val newPos = currentTime
            pos[x] = newPos
            symbolAtPos[newPos] = x
            bit.update(newPos, 1)
            currentTime++
        } else {
            val y = text[i]
            val totalActive = bit.query(maxPosValue)
            val k = totalActive - y + 1
            val targetPos = bit.findKth(k)
            val x = symbolAtPos[targetPos]
            result[i] = x
            bit.update(targetPos, -1)
            val newPos = currentTime
            pos[x] = newPos
            symbolAtPos[newPos] = x
            bit.update(newPos, 1)
            currentTime++
        }
    }
    writer.write(result.joinToString(" "))
    writer.newLine()
    reader.close()
    writer.close()
}
