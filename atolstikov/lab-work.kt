import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val st = StringTokenizer(reader.readLine())
    val n = st.nextToken().toInt()
    val x = st.nextToken().toLong()
    val k = st.nextToken().toLong()

    val a = LongArray(n)
    var S = 0L
    for (i in 0 until n) {
        val ai = reader.readLine().toLong()
        a[i] = ai
        S += ai
    }

    if (x == 0L) {
        val days = (S + k - 1) / k
        writer.write(days.toString())
        writer.newLine()
        writer.flush()
        return
    }

    var F = 0L
    val remList = LongArrayList()
    for (ai in a) {
        val qi = ai / x
        F += qi
        val ri = ai - qi * x
        if (ri > 0) remList.add(ri)
    }
    remList.sortDescending()
    val L = remList.size
    val prefixRem = LongArray(L + 1)
    for (i in 0 until L) {
        prefixRem[i + 1] = prefixRem[i] + remList[i]
    }

    val high = if (k > 0) {
        (S + k - 1) / k
    } else {
        F + L
    }

    var low = 0L
    var highBound = high
    while (low < highBound) {
        val mid = (low + highBound) / 2
        val D = mid
        val feasible = if (k > 0 && k * D >= S) {
            true
        } else {
            val G_req = if (k > 0) S - k * D else S
            when {
                D <= F -> {
                    val need = (G_req + x - 1) / x
                    D >= need
                }
                D <= F + L -> {
                    val extra = (D - F).toInt()
                    val gCap = F * x + prefixRem[extra]
                    gCap >= G_req
                }
                else -> {
                    true
                }
            }
        }
        if (feasible) {
            highBound = mid
        } else {
            low = mid + 1
        }
    }

    writer.write(low.toString())
    writer.newLine()
    writer.flush()
}

private class LongArrayList {
    private var data = LongArray(16)
    var size = 0
        private set

    fun add(v: Long) {
        if (size == data.size) {
            val newCap = data.size * 2
            data = data.copyOf(newCap)
        }
        data[size++] = v
    }

    fun sortDescending() {
        java.util.Arrays.sort(data, 0, size)
        var i = 0
        var j = size - 1
        while (i < j) {
            val t = data[i]
            data[i] = data[j]
            data[j] = t
            i++; j--
        }
    }

    operator fun get(index: Int): Long = data[index]
}
