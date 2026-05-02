import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.PriorityQueue

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val origAdj = Array<MutableList<Pair<Int, Int>>>(n + 1) { mutableListOf() }
    for (i in 1..n) {
        val parts = reader.readLine().split(' ')
        for (j in i + 1..n) {
            val w = parts[j - 1].toInt()
            if (w != -1) {
                origAdj[i].add(Pair(j, w))
                origAdj[j].add(Pair(i, w))
            }
        }
    }

    val INF = Long.MAX_VALUE
    val dist = LongArray(n + 1) { INF }
    dist[1] = 0L
    val pq = PriorityQueue<Pair<Long, Int>>(compareBy { it.first })
    pq.add(Pair(0L, 1))
    while (pq.isNotEmpty()) {
        val (d, u) = pq.poll()
        if (d != dist[u]) continue
        for ((v, w) in origAdj[u]) {
            val nd = d + w
            if (dist[v] > nd) {
                dist[v] = nd
                pq.add(Pair(nd, v))
            }
        }
    }

    val preds = Array<MutableList<Pair<Int, Int>>>(n + 1) { mutableListOf() }
    var edgeCount = 0
    for (u in 1..n) {
        if (dist[u] == INF) continue
        for ((v, w) in origAdj[u]) {
            if (dist[v] != INF && dist[u] + w == dist[v]) {
                preds[v].add(Pair(u, edgeCount++))
            }
        }
    }

    if (edgeCount == 0) {
        writer.write("0")
        writer.flush()
        return
    }

    val blocks = (edgeCount + 63) ushr 6
    val criticalEdges = Array(n + 1) { LongArray(blocks) }

    val order = (1..n).filter { dist[it] != INF }.sortedBy { dist[it] }
    for (v in order) {
        if (v == 1) continue
        val predList = preds[v]
        val firstU = predList[0].first
        val J = criticalEdges[firstU].copyOf()
        if (predList.size > 1) {
            for (i in 1 until predList.size) {
                val ui = predList[i].first
                val Ci = criticalEdges[ui]
                for (b in J.indices) {
                    J[b] = J[b] and Ci[b]
                }
            }
        }
        for ((_, eid) in predList) {
            val blockIndex = eid ushr 6
            val bitIndex = eid and 63
            val mask = 1L shl bitIndex
            if (J[blockIndex] and mask != 0L) continue
            var ok = true
            for ((uj, ej) in predList) {
                if (ej == eid) continue
                if (criticalEdges[uj][blockIndex] and mask == 0L) {
                    ok = false
                    break
                }
            }
            if (ok) {
                J[blockIndex] = J[blockIndex] or mask
            }
        }
        criticalEdges[v] = J
    }

    val cnt = IntArray(edgeCount)
    for (v in 1..n) {
        val arr = criticalEdges[v]
        for (b in arr.indices) {
            var word = arr[b]
            while (word != 0L) {
                val t = java.lang.Long.numberOfTrailingZeros(word)
                val idx = (b shl 6) + t
                cnt[idx]++
                word = word and (word - 1)
            }
        }
    }

    var ans = 0
    for (c in cnt) if (c > ans) ans = c
    writer.write(ans.toString())
    writer.flush()
}
