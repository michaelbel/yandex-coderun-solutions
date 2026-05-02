import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (n, m) = reader.readLine().split(' ').let { it[0].toInt() to it[1].toInt() }
    val (l, r) = reader.readLine().split(' ').let { it[0].toInt() to it[1].toInt() }

    val blueA = IntArray(n)
    val blueB = IntArray(n)
    repeat(n) { i ->
        reader.readLine().split(' ').let {
            blueA[i] = it[0].toInt()
            blueB[i] = it[1].toInt()
        }
    }

    val redC = IntArray(m)
    val redD = IntArray(m)
    var cMin = Int.MAX_VALUE
    var dMax = Int.MIN_VALUE
    repeat(m) { i ->
        reader.readLine().split(' ').let {
            val c = it[0].toInt()
            val d = it[1].toInt()
            redC[i] = c
            redD[i] = d
            if (c < cMin) cMin = c
            if (d > dMax) dMax = d
        }
    }

    val kMin = l - cMin
    val kMax = r - dMax

    data class Event(val pos: Int, val delta: Int)
    val events = ArrayList<Event>(4 * n * m)
    for (i in 0 until n) {
        val a = blueA[i]
        val b = blueB[i]
        for (j in 0 until m) {
            val c = redC[j]
            val d = redD[j]
            val start = a - d
            val mid1  = a - c
            val mid2  = b - d
            val end   = b - c
            val t1 = if (mid1 < mid2) mid1 else mid2
            val t2 = if (mid1 < mid2) mid2 else mid1
            events.add(Event(start, +1))
            events.add(Event(t1, -1))
            events.add(Event(t2, -1))
            events.add(Event(end, +1))
        }
    }
    events.sortBy { it.pos }

    var fValue = 0L
    for (i in 0 until n) {
        val a = blueA[i]
        val b = blueB[i]
        for (j in 0 until m) {
            val c = redC[j] + kMin
            val d = redD[j] + kMin
            val interStart = if (a > c) a else c
            val interEnd   = if (b < d) b else d
            if (interEnd > interStart) {
                fValue += (interEnd - interStart).toLong()
            }
        }
    }

    var slope = 0
    var idx = 0
    val size = events.size

    while (idx < size && events[idx].pos <= kMin) {
        slope += events[idx].delta
        idx++
    }

    var minF = fValue
    var prevK = kMin

    while (idx < size && events[idx].pos <= kMax) {
        val currK = events[idx].pos
        if (currK > prevK) {
            val dx = (currK - prevK).toLong()
            fValue += slope.toLong() * dx
            if (fValue < minF) minF = fValue
            prevK = currK
        }
        while (idx < size && events[idx].pos == currK) {
            slope += events[idx].delta
            idx++
        }
    }

    if (prevK < kMax) {
        val dx = (kMax - prevK).toLong()
        fValue += slope.toLong() * dx
        if (fValue < minF) minF = fValue
    }

    writer.write(minF.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
