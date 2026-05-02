import java.io.BufferedInputStream
import java.lang.StringBuilder

private const val LIMIT: Long = 1_000_000_000_000_000_000L

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    fun nextLong(): Long {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0L
        var ch = c
        if (ch == '-'.code) {
            sign = -1
            ch = readByte()
        }
        while (ch > 32 && ch >= 0) {
            res = res * 10 + (ch - '0'.code)
            ch = readByte()
        }
        return if (sign == 1) res else -res
    }

    fun nextInt(): Int = nextLong().toInt()
}

private class LongPairList(initialCap: Int = 4096) {
    var nums = LongArray(initialCap)
    var divs = LongArray(initialCap)
    var size = 0

    fun add(n: Long, d: Long) {
        if (size == nums.size) {
            val newCap = nums.size shl 1
            nums = nums.copyOf(newCap)
            divs = divs.copyOf(newCap)
        }
        nums[size] = n
        divs[size] = d
        size++
    }
}

private fun sortByNum(nums: LongArray, divs: LongArray, n: Int) {
    if (n <= 1) return
    val lStack = IntArray(64)
    val rStack = IntArray(64)
    var sp = 0
    lStack[sp] = 0
    rStack[sp] = n - 1
    sp++
    while (sp > 0) {
        sp--
        var l = lStack[sp]
        var r = rStack[sp]
        while (l < r) {
            var i = l
            var j = r
            val pivot = nums[(l + r) ushr 1]
            while (i <= j) {
                while (nums[i] < pivot) i++
                while (nums[j] > pivot) j--
                if (i <= j) {
                    val tn = nums[i]; nums[i] = nums[j]; nums[j] = tn
                    val td = divs[i]; divs[i] = divs[j]; divs[j] = td
                    i++
                    j--
                }
            }
            if (j - l < r - i) {
                if (i < r) {
                    lStack[sp] = i
                    rStack[sp] = r
                    sp++
                }
                r = j
            } else {
                if (l < j) {
                    lStack[sp] = l
                    rStack[sp] = j
                    sp++
                }
                l = i
            }
        }
    }
}

private fun lowerBound(a: LongArray, x: Long): Int {
    var l = 0
    var r = a.size
    while (l < r) {
        val m = (l + r) ushr 1
        if (a[m] < x) l = m + 1 else r = m
    }
    return l
}

private fun upperBound(a: LongArray, x: Long): Int {
    var l = 0
    var r = a.size
    while (l < r) {
        val m = (l + r) ushr 1
        if (a[m] <= x) l = m + 1 else r = m
    }
    return l
}

fun main() {
    val primes = intArrayOf(
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29,
        31, 37, 41, 43, 47, 53, 59
    )

    val cand = LongPairList(16384)

    fun dfs(idx: Int, maxExp: Int, value: Long, divCount: Long) {
        cand.add(value, divCount)
        if (idx >= primes.size) return
        val p = primes[idx].toLong()
        var v = value
        var e = 1
        while (e <= maxExp) {
            if (v > LIMIT / p) break
            v *= p
            dfs(idx + 1, e, v, divCount * (e + 1L))
            e++
        }
    }

    dfs(0, 60, 1L, 1L)

    sortByNum(cand.nums, cand.divs, cand.size)

    var best = 0L
    val artifactsTemp = LongArray(cand.size)
    var aSize = 0
    var i = 0
    while (i < cand.size) {
        val num = cand.nums[i]
        var d = cand.divs[i]
        var j = i + 1
        while (j < cand.size && cand.nums[j] == num) {
            if (cand.divs[j] > d) d = cand.divs[j]
            j++
        }
        if (d > best) {
            best = d
            artifactsTemp[aSize++] = num
        }
        i = j
    }
    val artifacts = artifactsTemp.copyOf(aSize)

    val fs = FastScanner()
    val q = fs.nextInt()
    val out = StringBuilder(q * 4)
    for (qq in 0 until q) {
        val l = fs.nextLong()
        val r = fs.nextLong()
        val ans = upperBound(artifacts, r) - lowerBound(artifacts, l)
        out.append(ans).append('\n')
    }
    print(out.toString())
}
