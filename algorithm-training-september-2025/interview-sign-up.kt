import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private class FastScanner(private val reader: BufferedReader) {
    private val buffer = CharArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun read(): Int {
        if (ptr >= len) {
            len = reader.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].code
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= 32 && c >= 0) {
            c = read()
        }
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = read()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + (c - 48)
            c = read()
        }
        return res * sign
    }

    fun nextInt(): Int = nextLong().toInt()
}

private class MinHeap(capacity: Int) {
    private val rArr = IntArray(capacity)
    private val remArr = LongArray(capacity)
    var size = 0
    var topR = 0
    var topRem = 0L

    fun clear() {
        size = 0
    }

    fun isEmpty(): Boolean = size == 0

    fun push(r: Int, rem: Long) {
        var i = size
        rArr[i] = r
        remArr[i] = rem
        size++
        while (i > 0) {
            val p = (i - 1) / 2
            if (rArr[p] <= rArr[i]) break
            var tmpR = rArr[p]
            rArr[p] = rArr[i]
            rArr[i] = tmpR
            val tmpRem = remArr[p]
            remArr[p] = remArr[i]
            remArr[i] = tmpRem
            i = p
        }
    }

    fun pop() {
        topR = rArr[0]
        topRem = remArr[0]
        size--
        if (size > 0) {
            rArr[0] = rArr[size]
            remArr[0] = remArr[size]
            var i = 0
            while (true) {
                val left = i * 2 + 1
                if (left >= size) break
                var smallest = left
                val right = left + 1
                if (right < size && rArr[right] < rArr[left]) {
                    smallest = right
                }
                if (rArr[i] <= rArr[smallest]) break
                var tmpR = rArr[i]
                rArr[i] = rArr[smallest]
                rArr[smallest] = tmpR
                val tmpRem = remArr[i]
                remArr[i] = remArr[smallest]
                remArr[smallest] = tmpRem
                i = smallest
            }
        }
    }
}

private var n = 0
private lateinit var a: LongArray
private lateinit var b: LongArray
private lateinit var heap: MinHeap
private var totalA = 0L
private var totalB = 0L

private fun feasible(k: Int): Boolean {
    if (totalA > totalB) return false

    heap.clear()
    var idx = 0

    for (day0 in 0 until n) {
        val day = day0 + 1

        // добавить группы кандидатов, которые уже "открылись"
        while (idx < n) {
            val i = idx + 1
            var l = i - k
            if (l < 1) l = 1
            if (l <= day) {
                val cnt = a[idx]
                if (cnt > 0L) {
                    var r = i + k
                    if (r > n) r = n
                    heap.push(r, cnt)
                }
                idx++
            } else {
                break
            }
        }

        var cap = b[day0]
        if (cap <= 0L) continue

        while (cap > 0L && !heap.isEmpty()) {
            heap.pop()
            val r = heap.topR
            var rem = heap.topRem
            if (r < day) {
                return false
            }
            if (rem <= cap) {
                cap -= rem
            } else {
                rem -= cap
                cap = 0L
                heap.push(r, rem)
            }
        }
    }

    if (!heap.isEmpty()) return false
    while (idx < n) {
        if (a[idx] > 0L) return false
        idx++
    }
    return true
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    n = fs.nextInt()
    a = LongArray(n)
    b = LongArray(n)

    totalA = 0L
    totalB = 0L

    for (i in 0 until n) {
        val v = fs.nextLong()
        a[i] = v
        totalA += v
    }
    for (i in 0 until n) {
        val v = fs.nextLong()
        b[i] = v
        totalB += v
    }

    if (totalA > totalB) {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }

    heap = MinHeap(2 * n + 5)

    var left = 0
    var right = n
    while (left < right) {
        val mid = (left + right) ushr 1
        if (feasible(mid)) {
            right = mid
        } else {
            left = mid + 1
        }
    }

    writer.write(left.toString())
    writer.newLine()
    writer.flush()
}
