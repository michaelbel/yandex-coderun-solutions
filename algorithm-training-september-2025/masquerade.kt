import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine() ?: run {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }
    val st = StringTokenizer(firstLine)
    val n = st.nextToken().toInt()
    val lNeed = st.nextToken().toInt()

    val P = IntArray(n + 1)
    val R = IntArray(n + 1)
    val Q = IntArray(n + 1)
    val F = IntArray(n + 1)

    var totalCap = 0
    for (i in 1..n) {
        val line = StringTokenizer(reader.readLine())
        P[i] = line.nextToken().toInt()
        R[i] = line.nextToken().toInt()
        Q[i] = line.nextToken().toInt()
        F[i] = line.nextToken().toInt()
        totalCap += F[i]
    }

    // Если нужно 0 метров — ничего не покупаем
    if (lNeed == 0) {
        writer.write("0")
        writer.newLine()
        val sb = StringBuilder()
        for (i in 0 until n) {
            if (i > 0) sb.append(' ')
            sb.append('0')
        }
        writer.write(sb.toString())
        writer.newLine()
        writer.flush()
        return
    }

    // Если ткани точно не хватит
    if (totalCap < lNeed) {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }

    // DP по магазинам и количеству ткани
    val cap = totalCap
    val INF = 1_000_000_000

    // dpPrev[v] — минимальная стоимость купить ровно v метров из первых i-1 магазинов
    var dpPrev = IntArray(cap + 1) { INF }
    var dpCur = IntArray(cap + 1) { INF }
    dpPrev[0] = 0

    // choice[i][v] — сколько метров взяли в i-м магазине, если для первых i магазинов оптимально набрать v метров
    val choice = Array(n + 1) { IntArray(cap + 1) }

    // Предварительно посчитаем стоимость x метров в каждом магазине
    val shopCost = Array(n + 1) { IntArray(0) }
    for (i in 1..n) {
        val fi = F[i]
        val costArr = IntArray(fi + 1)
        costArr[0] = 0
        val pi = P[i]
        val ri = R[i]
        val qi = Q[i]
        for (x in 1..fi) {
            costArr[x] = if (x < ri) pi * x else qi * x
        }
        shopCost[i] = costArr
    }

    for (i in 1..n) {
        val fi = F[i]
        val costArr = shopCost[i]
        for (v in 0..cap) {
            dpCur[v] = INF
        }

        for (v in 0..cap) {
            if (dpPrev[v] == INF) continue
            // берем x метров в i-м магазине
            val maxTake = if (v + fi <= cap) fi else cap - v
            var x = 0
            while (x <= maxTake) {
                val newV = v + x
                val cand = dpPrev[v] + costArr[x]
                if (cand < dpCur[newV]) {
                    dpCur[newV] = cand
                    choice[i][newV] = x
                }
                x++
            }
        }

        val tmp = dpPrev
        dpPrev = dpCur
        dpCur = tmp
    }

    // Ищем лучший вариант среди суммарных метров >= lNeed
    var bestCost = INF
    var bestV = -1
    for (v in lNeed..cap) {
        if (dpPrev[v] < bestCost) {
            bestCost = dpPrev[v]
            bestV = v
        }
    }

    // Теоретически должно существовать решение (totalCap >= lNeed)
    if (bestV == -1 || bestCost == INF) {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }

    // Восстановим, сколько покупали в каждом магазине
    val result = IntArray(n + 1)
    var vCur = bestV
    for (i in n downTo 1) {
        val x = choice[i][vCur]
        result[i] = x
        vCur -= x
    }

    writer.write(bestCost.toString())
    writer.newLine()
    val sb = StringBuilder()
    for (i in 1..n) {
        if (i > 1) sb.append(' ')
        sb.append(result[i])
    }
    writer.write(sb.toString())
    writer.newLine()
    writer.flush()
}
