import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Locale
import java.util.PriorityQueue
import java.util.StringTokenizer

private class FastScanner(private val reader: BufferedReader) {
    private var tokenizer: StringTokenizer? = null

    fun next(): String {
        while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    fun nextInt(): Int = next().toInt()
}

private fun encode(loc: Int, hasS: Int, hasP: Int, delS: Int, delP: Int): Int {
    var x = loc
    x = (x shl 1) or hasS
    x = (x shl 1) or hasP
    x = (x shl 1) or delS
    x = (x shl 1) or delP
    return x
}

private fun minimalTime(a: Int, b: Int, c: Int, v0: Int, v1: Int, v2: Int): Double {
    val inf = 1e100
    val stateCount = 3 * 16
    val dist = DoubleArray(stateCount) { inf }

    val start = encode(0, 0, 0, 0, 0)
    val target = encode(0, 0, 0, 1, 1)
    dist[start] = 0.0

    // Neighbours: for each location store (next, length) pairs
    val neigh = arrayOf(
        intArrayOf(1, a, 2, b), // from home
        intArrayOf(0, a, 2, c), // from shop
        intArrayOf(0, b, 1, c)  // from pickup point
    )

    val pq = PriorityQueue<Pair<Double, Int>>(compareBy { it.first })
    pq.add(0.0 to start)

    val eps = 1e-12

    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d > dist[u] + eps) continue
        if (u == target) {
            return d
        }

        var x = u
        val delP = x and 1
        x = x shr 1
        val delS = x and 1
        x = x shr 1
        val hasP = x and 1
        x = x shr 1
        val hasS = x and 1
        x = x shr 1
        val loc = x

        val load = hasS + hasP
        val v = when (load) {
            0 -> v0
            1 -> v1
            else -> v2
        }

        // Move along roads
        val e = neigh[loc]
        var idx = 0
        while (idx < e.size) {
            val nxt = e[idx]
            val len = e[idx + 1]
            idx += 2
            val w = d + len.toDouble() / v.toDouble()
            val vid = encode(nxt, hasS, hasP, delS, delP)
            if (w + eps < dist[vid]) {
                dist[vid] = w
                pq.add(w to vid)
            }
        }

        // Pick up at shop (products)
        if (loc == 1 && delS == 0 && hasS == 0) {
            val vid = encode(loc, 1, hasP, delS, delP)
            if (d + eps < dist[vid]) {
                dist[vid] = d
                pq.add(d to vid)
            }
        }

        // Pick up at pickup point (parcel)
        if (loc == 2 && delP == 0 && hasP == 0) {
            val vid = encode(loc, hasS, 1, delS, delP)
            if (d + eps < dist[vid]) {
                dist[vid] = d
                pq.add(d to vid)
            }
        }

        // Drop at home
        if (loc == 0) {
            if (hasS == 1 && delS == 0) {
                val vid = encode(loc, 0, hasP, 1, delP)
                if (d + eps < dist[vid]) {
                    dist[vid] = d
                    pq.add(d to vid)
                }
            }
            if (hasP == 1 && delP == 0) {
                val vid = encode(loc, hasS, 0, delS, 1)
                if (d + eps < dist[vid]) {
                    dist[vid] = d
                    pq.add(d to vid)
                }
            }
        }
    }

    return dist[target]
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val scanner = FastScanner(reader)

    val a = scanner.nextInt()
    val b = scanner.nextInt()
    val c = scanner.nextInt()
    val v0 = scanner.nextInt()
    val v1 = scanner.nextInt()
    val v2 = scanner.nextInt()

    val ans = minimalTime(a, b, c, v0, v1, v2)
    writer.write(String.format(Locale.US, "%.15f", ans))
    writer.newLine()
    writer.flush()
}
