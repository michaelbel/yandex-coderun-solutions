import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstSt = StringTokenizer(reader.readLine())
    val n = firstSt.nextToken().toInt()
    val k = firstSt.nextToken().toInt()

    val a = IntArray(n + 1)
    val st = StringTokenizer(reader.readLine())
    for (i in 1..n) {
        a[i] = st.nextToken().toInt()
    }

    // Префиксные суммы
    val pref = LongArray(n + 1)
    for (i in 1..n) {
        pref[i] = pref[i - 1] + a[i].toLong()
    }

    val maxStart = n - k + 1
    val towerValue = LongArray(n + 2)

    // Предварительно считаем ценность каждой башни длины K, начинающейся в позиции i
    if (k <= n) {
        for (i in 1..maxStart) {
            val sum = pref[i + k - 1] - pref[i - 1]
            var mn = a[i]
            var j = i + 1
            val end = i + k - 1
            while (j <= end) {
                if (a[j] < mn) mn = a[j]
                j++
            }
            towerValue[i] = sum * mn.toLong()
        }
    }

    // DP по позициям: dp[i] — максимум от столбика i до конца
    val dp = LongArray(n + 2)
    val take = BooleanArray(n + 2)

    for (i in n downTo 1) {
        if (i <= maxStart) {
            val withTower = towerValue[i] + dp[i + k]
            val withoutTower = dp[i + 1]
            if (withTower > withoutTower) {
                dp[i] = withTower
                take[i] = true
            } else {
                dp[i] = withoutTower
                take[i] = false
            }
        } else {
            dp[i] = dp[i + 1]
            take[i] = false
        }
    }

    // Восстановление ответа
    val starts = ArrayList<Int>()
    var pos = 1
    while (pos <= maxStart) {
        if (take[pos]) {
            starts.add(pos)
            pos += k
        } else {
            pos++
        }
    }

    writer.write(starts.size.toString())
    writer.newLine()
    if (starts.isNotEmpty()) {
        val sb = StringBuilder()
        for (i in starts.indices) {
            if (i > 0) sb.append(' ')
            sb.append(starts[i])
        }
        writer.write(sb.toString())
        writer.newLine()
    }
    writer.flush()
}
