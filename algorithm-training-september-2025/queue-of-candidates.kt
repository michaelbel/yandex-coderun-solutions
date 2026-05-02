import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class Fenwick(private val n: Int) {
    private val bit = IntArray(n + 2)

    fun add(pos0: Int, delta: Int) {
        var i = pos0 + 1
        while (i <= n + 1) {
            bit[i] += delta
            i += i and -i
        }
    }

    fun sum(pos0: Int): Int {
        var i = pos0 + 1
        var res = 0
        while (i > 0) {
            res += bit[i]
            i -= i and -i
        }
        return res
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val first = StringTokenizer(reader.readLine())
    val n = first.nextToken().toInt()
    val x = first.nextToken().toLong()

    val initTokens = StringTokenizer(reader.readLine())
    val m = reader.readLine().trim().toInt()

    // максимум людей в очереди: начальные + все возможные добавления
    val maxSize = n + m
    val fenwick = Fenwick(maxSize)

    var nextPos = 0        // следующая позиция для добавления в общей ленте
    var startIdx = 0       // индекс начала текущей очереди в общей ленте
    var queueSize = n      // текущий размер очереди

    // начальная очередь
    repeat(n) {
        val v = initTokens.nextToken().toLong()
        val good = if (v >= x) 1 else 0
        fenwick.add(nextPos, good)
        nextPos++
    }

    val out = StringBuilder()

    repeat(m) {
        val st = StringTokenizer(reader.readLine())
        val type = st.nextToken().toInt()
        when (type) {
            1 -> { // push back
                val v = st.nextToken().toLong()
                val good = if (v >= x) 1 else 0
                fenwick.add(nextPos, good)
                nextPos++
                queueSize++
            }
            2 -> { // pop front
                // гарантировано, что очередь не пуста
                startIdx++
                queueSize--
            }
            3 -> { // query
                val k = st.nextToken().toInt()  // среди первых k человек
                if (k == 0) {
                    out.append("0\n")
                } else {
                    val l = startIdx
                    val r = startIdx + k - 1
                    val sumR = fenwick.sum(r)
                    val sumL = if (l > 0) fenwick.sum(l - 1) else 0
                    val ans = sumR - sumL
                    out.append(ans).append('\n')
                }
            }
        }
    }

    writer.write(out.toString())
    writer.flush()
}
