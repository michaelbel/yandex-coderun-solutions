import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.max

lateinit var segTree: IntArray
var m = 0

fun update(pos0: Int, value: Int) {
    var pos = pos0 + m
    segTree[pos] = max(segTree[pos], value)
    while (pos > 1) {
        pos /= 2
        segTree[pos] = max(segTree[pos * 2], segTree[pos * 2 + 1])
    }
}

fun query(l0: Int, r0: Int): Int {
    var l = l0 + m
    var r = r0 + m
    var res = 0
    while (l <= r) {
        if (l % 2 == 1) {
            res = max(res, segTree[l])
            l++
        }
        if (r % 2 == 0) {
            res = max(res, segTree[r])
            r--
        }
        l /= 2
        r /= 2
    }
    return res
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    val b = st.nextToken().toInt()
    val arr = IntArray(n)
    st = StringTokenizer(reader.readLine())
    for (i in 0 until n) {
        arr[i] = st.nextToken().toInt()
    }
    var size = n + 1
    m = 1
    while (m < size) {
        m = m shl 1
    }
    segTree = IntArray(2 * m)
    var ans = 0
    for (i in 0 until n) {
        val a = arr[i]
        var L = a - b
        if (L < 0) {
            L = 0
        }
        val best = query(L, a)
        val dp = best + 1
        update(a, dp)
        if (dp > ans) {
            ans = dp
        }
    }
    writer.write(ans.toString())
    writer.flush()
    reader.close()
    writer.close()
}

