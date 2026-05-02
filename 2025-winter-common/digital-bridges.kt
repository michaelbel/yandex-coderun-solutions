import java.io.BufferedInputStream
import java.lang.StringBuilder
import java.util.PriorityQueue
import kotlin.math.min

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

    fun nextInt(): Int {
        var c: Int
        do {
            c = readByte()
        } while (c <= 32 && c >= 0)
        var sign = 1
        var res = 0
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

    fun nextLong(): Long = nextInt().toLong()
}

private class MinCostMaxFlow(private val n: Int) {
    private class Edge(var to: Int, var rev: Int, var cap: Int, var cost: Long)

    private val g = Array(n) { ArrayList<Edge>() }

    fun addEdge(fr: Int, to: Int, cap: Int, cost: Long) {
        val fwd = Edge(to, g[to].size, cap, cost)
        val rev = Edge(fr, g[fr].size, 0, -cost)
        g[fr].add(fwd)
        g[to].add(rev)
    }

    fun minCostFlow(s: Int, t: Int, maxf: Int): LongArray {
        val INF = Long.MAX_VALUE / 4
        val h = LongArray(n)
        val dist = LongArray(n)
        val pv = IntArray(n)
        val pe = IntArray(n)

        var flow = 0
        var cost = 0L

        val pq = PriorityQueue<LongArray>(Comparator { a, b ->
            when {
                a[0] < b[0] -> -1
                a[0] > b[0] -> 1
                else -> 0
            }
        })

        while (flow < maxf) {
            java.util.Arrays.fill(dist, INF)
            dist[s] = 0L
            pq.clear()
            pq.add(longArrayOf(0L, s.toLong()))
            while (pq.isNotEmpty()) {
                val cur = pq.poll()
                val d = cur[0]
                val v = cur[1].toInt()
                if (d != dist[v]) continue
                val edges = g[v]
                for (i in edges.indices) {
                    val e = edges[i]
                    if (e.cap <= 0) continue
                    val nd = d + e.cost + h[v] - h[e.to]
                    if (nd < dist[e.to]) {
                        dist[e.to] = nd
                        pv[e.to] = v
                        pe[e.to] = i
                        pq.add(longArrayOf(nd, e.to.toLong()))
                    }
                }
            }
            if (dist[t] == INF) break
            for (v in 0 until n) if (dist[v] < INF) h[v] += dist[v]

            var add = maxf - flow
            var v = t
            while (v != s) {
                val e = g[pv[v]][pe[v]]
                add = min(add, e.cap)
                v = pv[v]
            }

            v = t
            while (v != s) {
                val e = g[pv[v]][pe[v]]
                e.cap -= add
                g[v][e.rev].cap += add
                cost += e.cost * add.toLong()
                v = pv[v]
            }
            flow += add
        }
        return longArrayOf(flow.toLong(), cost)
    }
}

fun main() {
    val fs = FastScanner()
    val t = fs.nextInt()
    val out = StringBuilder(t * 16)

    repeat(t) {
        val n = fs.nextInt()
        val m = fs.nextInt()
        val a = LongArray(n)
        for (i in 0 until n) a[i] = fs.nextLong()
        val b = LongArray(m)
        for (i in 0 until m) b[i] = fs.nextLong()

        val base = n / m

        val S = 0
        val elemStart = 1
        val groupStart = elemStart + n
        val T = groupStart + m
        val SS = T + 1
        val TT = T + 2
        val nodes = TT + 1

        val demand = LongArray(nodes)
        val mcmf = MinCostMaxFlow(nodes)

        fun addEdgeLower(fr: Int, to: Int, lower: Int, upper: Int, cost: Long) {
            val cap = upper - lower
            if (cap > 0) mcmf.addEdge(fr, to, cap, cost)
            if (lower != 0) {
                demand[fr] -= lower.toLong()
                demand[to] += lower.toLong()
            }
        }

        for (i in 0 until n) {
            addEdgeLower(S, elemStart + i, 1, 1, 0L)
        }

        for (i in 0 until n) {
            val ai = a[i]
            val u = elemStart + i
            for (j in 0 until m) {
                val bj = b[j]
                val rem = ai % bj
                val add = if (rem == 0L) 0L else bj - rem
                mcmf.addEdge(u, groupStart + j, 1, add)
            }
        }

        for (j in 0 until m) {
            val gNode = groupStart + j
            addEdgeLower(gNode, T, base, base + 1, 0L)
        }

        addEdgeLower(T, S, n, n, 0L)

        var need = 0
        for (v in 0 until nodes) {
            val d = demand[v]
            if (d > 0) {
                mcmf.addEdge(SS, v, d.toInt(), 0L)
                need += d.toInt()
            } else if (d < 0) {
                mcmf.addEdge(v, TT, (-d).toInt(), 0L)
            }
        }

        val res = mcmf.minCostFlow(SS, TT, need)
        out.append(res[1]).append('\n')
    }

    print(out.toString())
}
