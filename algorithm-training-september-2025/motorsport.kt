import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.PriorityQueue

private class FastScanner(private val reader: BufferedReader) {
    private var st: StringTokenizer? = null

    fun next(): String {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            st = StringTokenizer(line)
        }
        return st!!.nextToken()
    }

    fun nextInt(): Int = next().toInt()
    fun nextLong(): Long = next().toLong()
}

private const val TYPE_FINISH = 0
private const val TYPE_WALL = 1
private const val TYPE_COLLISION = 2

private class Event(
    val num: Long,   // время как рациональное num / den
    val den: Long,
    val type: Int,
    val i: Int,
    val j: Int
)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val out = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val L = fs.nextLong()
    val W = fs.nextLong()

    val x = LongArray(n)
    val y = LongArray(n)
    val vx = LongArray(n)
    val vy = LongArray(n)

    for (i in 0 until n) {
        x[i] = fs.nextLong()
        y[i] = fs.nextLong()
        vx[i] = fs.nextLong()
        vy[i] = fs.nextLong()
    }

    // очередь событий, сортируем по времени (num / den)
    val pq = PriorityQueue<Event> { e1, e2 ->
        val lhs = e1.num * e2.den
        val rhs = e2.num * e1.den
        when {
            lhs < rhs -> -1
            lhs > rhs -> 1
            else -> e1.type - e2.type
        }
    }

    fun addEvent(numRaw: Long, denRaw: Long, type: Int, i: Int, j: Int) {
        if (denRaw == 0L) return
        var num = numRaw
        var den = denRaw
        if (den < 0) {
            den = -den
            num = -num
        }
        if (num < 0) return   // события в прошлом не нужны
        // num == 0 возможно, но стартовые точки не на границах, так что не случится
        pq.add(Event(num, den, type, i, j))
    }

    // события пересечения финиша и ударов о борта
    for (i in 0 until n) {
        if (vx[i] > 0L) {
            val num = L - x[i]
            if (num >= 0L) addEvent(num, vx[i], TYPE_FINISH, i, -1)
        }
        if (vy[i] > 0L) {
            val num = W - y[i]
            if (num > 0L) addEvent(num, vy[i], TYPE_WALL, i, -1)
        } else if (vy[i] < 0L) {
            val num = y[i]
            if (num > 0L) addEvent(num, -vy[i], TYPE_WALL, i, -1)
        }
    }

    // события столкновений
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            val dxv = vx[i] - vx[j]
            val dyv = vy[i] - vy[j]
            val sx = x[j] - x[i]
            val sy = y[j] - y[i]

            if (dxv == 0L && dyv == 0L) continue

            if (dxv == 0L) {
                if (sx != 0L) continue
                if (dyv == 0L) continue
                // x одинаково всегда, решаем по y
                var num = sy
                var den = dyv
                if (den < 0) {
                    den = -den
                    num = -num
                }
                if (num < 0L) continue
                addEvent(num, den, TYPE_COLLISION, i, j)
            } else if (dyv == 0L) {
                if (sy != 0L) continue
                var num = sx
                var den = dxv
                if (den < 0) {
                    den = -den
                    num = -num
                }
                if (num < 0L) continue
                addEvent(num, den, TYPE_COLLISION, i, j)
            } else {
                // обе компоненты скорости отличаются, проверяем совместимость
                if (dxv * sy != dyv * sx) continue
                var num = sx
                var den = dxv
                if (den < 0) {
                    den = -den
                    num = -num
                }
                if (num < 0L) continue
                addEvent(num, den, TYPE_COLLISION, i, j)
            }
        }
    }

    val alive = BooleanArray(n) { true }

    val crashStamp = IntArray(n)
    var crashStampNow = 0
    val finishStamp = IntArray(n)
    var finishStampNow = 0

    val crashedIdx = IntArray(n)
    val finishIdx = IntArray(n)

    var winnersFound = false
    var winners: IntArray = IntArray(0)

    while (!pq.isEmpty() && !winnersFound) {
        val first = pq.poll()
        val baseNum = first.num
        val baseDen = first.den

        crashStampNow++
        finishStampNow++
        var crashedCnt = 0
        var finishCnt = 0

        fun process(e: Event) {
            when (e.type) {
                TYPE_FINISH -> {
                    val i = e.i
                    if (!alive[i]) return
                    if (finishStamp[i] != finishStampNow) {
                        finishStamp[i] = finishStampNow
                        finishIdx[finishCnt++] = i
                    }
                }
                TYPE_WALL -> {
                    val i = e.i
                    if (!alive[i]) return
                    if (crashStamp[i] != crashStampNow) {
                        crashStamp[i] = crashStampNow
                        crashedIdx[crashedCnt++] = i
                    }
                }
                TYPE_COLLISION -> {
                    val i = e.i
                    val j = e.j
                    if (!alive[i] || !alive[j]) return
                    if (crashStamp[i] != crashStampNow) {
                        crashStamp[i] = crashStampNow
                        crashedIdx[crashedCnt++] = i
                    }
                    if (crashStamp[j] != crashStampNow) {
                        crashStamp[j] = crashStampNow
                        crashedIdx[crashedCnt++] = j
                    }
                }
            }
        }

        process(first)

        while (!pq.isEmpty()) {
            val e = pq.peek()
            val lhs = e.num * baseDen
            val rhs = baseNum * e.den
            if (lhs == rhs) {
                pq.poll()
                process(e)
            } else break
        }

        // сначала все столкнувшиеся/врезавшиеся выбывают
        for (idx in 0 until crashedCnt) {
            val car = crashedIdx[idx]
            alive[car] = false
        }

        // затем смотрим, кто финишировал в этот же момент и ещё жив
        var cnt = 0
        val tmp = IntArray(finishCnt)
        for (idx in 0 until finishCnt) {
            val car = finishIdx[idx]
            if (alive[car]) {
                tmp[cnt++] = car
            }
        }

        if (cnt > 0) {
            winnersFound = true
            tmp.sort(0, cnt)
            winners = tmp.copyOf(cnt)
        }
    }

    if (!winnersFound) {
        out.write("0")
        out.newLine()
        out.newLine()
    } else {
        out.write(winners.size.toString())
        out.newLine()
        val sb = StringBuilder()
        for (i in winners.indices) {
            if (i > 0) sb.append(' ')
            sb.append(winners[i] + 1) // номера моделей с 1
        }
        out.write(sb.toString())
        out.newLine()
    }

    out.flush()
}
