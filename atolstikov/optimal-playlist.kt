import java.io.BufferedInputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter

data class Edge(val u: Int, val v: Int, val w: Int)

fun main() {
    val input = BufferedInputStream(System.`in`)
    val buf = ByteArray(1 shl 16)
    var bufPos = 0
    var bufRead = 0
    fun read(): Int {
        if (bufPos >= bufRead) {
            bufRead = input.read(buf)
            bufPos = 0
            if (bufRead == -1) return -1
        }
        return buf[bufPos++].toInt()
    }
    fun readInt(): Int {
        var c: Int
        do {
            c = read()
            if (c == -1) return c
        } while (c <= ' '.code)
        var neg = false
        if (c == '-'.code) {
            neg = true
            c = read()
        }
        var res = 0
        while (c >= '0'.code && c <= '9'.code) {
            res = res * 10 + (c - '0'.code)
            c = read()
        }
        return if (neg) -res else res
    }

    val n = readInt()
    val m = readInt()
    if (n <= 1) {
        BufferedWriter(OutputStreamWriter(System.out)).use { out ->
            out.write("0")
            out.flush()
        }
        return
    }

    val edges = ArrayList<Edge>(m)
    repeat(m) {
        val u = readInt() - 1
        val v = readInt() - 1
        val w = readInt()
        edges.add(Edge(u, v, w))
    }
    edges.sortBy { it.w }

    val headF     = IntArray(n)
    val headR     = IntArray(n)
    val nextF     = IntArray(m)
    val toF       = IntArray(m)
    val nextR     = IntArray(m)
    val toR       = IntArray(m)
    val orderList = IntArray(n)
    val visited   = BooleanArray(n)
    val stack     = IntArray(n)
    val stackPtr  = IntArray(n)

    val comp      = IntArray(n)
    val stack2    = IntArray(n)

    val headC     = IntArray(n)
    val nextC     = IntArray(m)
    val toC       = IntArray(m)
    val indegreeC = IntArray(n)
    val queue     = IntArray(n)
    val dp        = IntArray(n)

    fun check(mid: Int): Boolean {
        for (i in 0 until n) {
            headF[i] = -1
            headR[i] = -1
            visited[i] = false
        }
        for (eIdx in 0..mid) {
            val e = edges[eIdx]
            toF[eIdx] = e.v
            nextF[eIdx] = headF[e.u]
            headF[e.u] = eIdx
            toR[eIdx] = e.u
            nextR[eIdx] = headR[e.v]
            headR[e.v] = eIdx
        }

        var orderSize = 0
        for (i in 0 until n) {
            if (!visited[i]) {
                var sp = 0
                stack[sp] = i
                stackPtr[sp] = headF[i]
                visited[i] = true
                sp++
                while (sp > 0) {
                    val v = stack[sp - 1]
                    val ePtr = stackPtr[sp - 1]
                    if (ePtr != -1) {
                        stackPtr[sp - 1] = nextF[ePtr]
                        val u = toF[ePtr]
                        if (!visited[u]) {
                            visited[u] = true
                            stack[sp] = u
                            stackPtr[sp] = headF[u]
                            sp++
                        }
                    } else {
                        sp--
                        orderList[orderSize++] = v
                    }
                }
            }
        }

        for (i in 0 until n) comp[i] = -1
        var compCnt = 0
        for (idx in orderSize - 1 downTo 0) {
            val v = orderList[idx]
            if (comp[v] == -1) {
                var sp2 = 0
                comp[v] = compCnt
                stack2[sp2++] = v
                while (sp2 > 0) {
                    val u = stack2[--sp2]
                    var ePtr = headR[u]
                    while (ePtr != -1) {
                        val w = toR[ePtr]
                        if (comp[w] == -1) {
                            comp[w] = compCnt
                            stack2[sp2++] = w
                        }
                        ePtr = nextR[ePtr]
                    }
                }
                compCnt++
            }
        }
        if (compCnt == 1) return true

        for (i in 0 until compCnt) {
            headC[i] = -1
            indegreeC[i] = 0
        }
        var edgeC = 0
        for (eIdx in 0..mid) {
            val e = edges[eIdx]
            val cu = comp[e.u]
            val cv = comp[e.v]
            if (cu != cv) {
                toC[edgeC] = cv
                nextC[edgeC] = headC[cu]
                headC[cu] = edgeC
                indegreeC[cv]++
                edgeC++
            }
        }

        var ql = 0
        var qr = 0
        for (i in 0 until compCnt) {
            dp[i] = 1
            if (indegreeC[i] == 0) {
                queue[qr++] = i
            }
        }
        var best = 1
        while (ql < qr) {
            val u = queue[ql++]
            val du = dp[u]
            var ePtr = headC[u]
            while (ePtr != -1) {
                val v = toC[ePtr]
                val cand = du + 1
                if (dp[v] < cand) {
                    dp[v] = cand
                    if (cand > best) best = cand
                }
                if (--indegreeC[v] == 0) {
                    queue[qr++] = v
                }
                ePtr = nextC[ePtr]
            }
        }

        return best >= compCnt
    }

    var left = 0
    var right = m - 1
    var answer = m
    while (left <= right) {
        val mid = (left + right) ushr 1
        if (check(mid)) {
            answer = mid
            right = mid - 1
        } else {
            left = mid + 1
        }
    }

    BufferedWriter(OutputStreamWriter(System.out)).use { out ->
        if (answer == m) {
            out.write("-1")
        } else {
            out.write(edges[answer].w.toString())
        }
        out.flush()
    }
}
