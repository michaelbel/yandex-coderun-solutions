import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import java.util.Arrays
import kotlin.math.min

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    var tokenizer = StringTokenizer("")

    fun next(): String {
        while (!tokenizer.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer.nextToken()
    }

    fun nextInt(): Int {
        val s = next()
        if (s == "") return 0
        return s.toInt()
    }

    val nStr = next()
    if (nStr == "") return
    val n = nStr.toInt()
    val m = nextInt()

    val tax = IntArray(n + 1)
    for (i in 1..n) {
        tax[i] = nextInt()
    }

    val head = IntArray(n + 1) { -1 }
    val to = IntArray(m)
    val nextEdge = IntArray(m)
    
    val headR = IntArray(n + 1) { -1 }
    val toR = IntArray(m)
    val nextEdgeR = IntArray(m)

    for (i in 0 until m) {
        val u = nextInt()
        val v = nextInt()
        
        to[i] = v
        nextEdge[i] = head[u]
        head[u] = i
        
        toR[i] = u
        nextEdgeR[i] = headR[v]
        headR[v] = i
    }

    // Kosaraju Step 1
    val order = IntArray(n)
    var orderIdx = 0
    val visited = BooleanArray(n + 1)
    val stackNode = IntArray(n + 1)
    val stackEdge = IntArray(n + 1)
    var sp: Int

    for (i in 1..n) {
        if (!visited[i]) {
            sp = 0
            stackNode[0] = i
            stackEdge[0] = head[i]
            visited[i] = true
            
            while (sp >= 0) {
                val u = stackNode[sp]
                var e = stackEdge[sp]
                
                var pushed = false
                while (e != -1) {
                    val v = to[e]
                    e = nextEdge[e]
                    if (!visited[v]) {
                        visited[v] = true
                        stackEdge[sp] = e 
                        sp++
                        stackNode[sp] = v
                        stackEdge[sp] = head[v]
                        pushed = true
                        break
                    }
                }
                
                if (!pushed) {
                    order[orderIdx++] = u
                    sp--
                }
            }
        }
    }

    // Kosaraju Step 2
    val comp = IntArray(n + 1)
    var compCount = 0
    
    for (i in n - 1 downTo 0) {
        val root = order[i]
        if (comp[root] == 0) {
            compCount++
            comp[root] = compCount
            
            sp = 0
            stackNode[0] = root
            
            while (sp >= 0) {
                val u = stackNode[sp]
                sp--
                
                var e = headR[u]
                while (e != -1) {
                    val v = toR[e]
                    if (comp[v] == 0) {
                        comp[v] = compCount
                        sp++
                        stackNode[sp] = v
                    }
                    e = nextEdgeR[e]
                }
            }
        }
    }

    val startComp = comp[1]
    val endComp = comp[n]
    val compSize = IntArray(compCount + 1)
    for (i in 1..n) compSize[comp[i]]++

    val INF = 2000000000
    var firstStepInsideStart = INF
    val directToComp = IntArray(compCount + 1) { INF }

    var eIdx = head[1]
    while (eIdx != -1) {
        val v = to[eIdx]
        val cv = comp[v]
        val cost = tax[v]
        if (cv == startComp) {
            if (cost < firstStepInsideStart) firstStepInsideStart = cost
        } else {
            if (cost < directToComp[cv]) directToComp[cv] = cost
        }
        eIdx = nextEdge[eIdx]
    }

    if (startComp == endComp) {
        if (n == 1) println(0) else println(firstStepInsideStart)
        return
    }

    val edges = LongArray(m)
    var edgeCount = 0
    
    for (u in 1..n) {
        val uComp = comp[u]
        var idx = head[u]
        while (idx != -1) {
            val v = to[idx]
            val vComp = comp[v]
            if (uComp != vComp) {
                val w = tax[v]
                val entry = (uComp.toLong() shl 44) or (vComp.toLong() shl 24) or w.toLong()
                edges[edgeCount++] = entry
            }
            idx = nextEdge[idx]
        }
    }
    
    val validEdges = edges.copyOfRange(0, edgeCount)
    validEdges.sort()

    val dagHead = IntArray(compCount + 1) { -1 }
    val dagTo = IntArray(edgeCount)
    val dagW = IntArray(edgeCount)
    val dagNext = IntArray(edgeCount)
    var dagEdgeCount = 0
    
    var currentU = -1
    var currentV = -1
    
    for (packed in validEdges) {
        val u = (packed ushr 44).toInt()
        val v = ((packed ushr 24) and 0xFFFFF).toInt()
        val w = (packed and 0xFFFFFF).toInt()
        
        if (u == currentU && v == currentV) continue
        
        currentU = u
        currentV = v
        
        dagTo[dagEdgeCount] = v
        dagW[dagEdgeCount] = w
        dagNext[dagEdgeCount] = dagHead[u]
        dagHead[u] = dagEdgeCount
        dagEdgeCount++
    }

    val dist = IntArray(compCount + 1) { INF }
    
    if (compSize[startComp] == 1) {
        dist[startComp] = 0
    } else {
        if (firstStepInsideStart != INF) {
            dist[startComp] = firstStepInsideStart
        }
    }
    
    for (c in 1..compCount) {
        if (directToComp[c] != INF) {
            if (directToComp[c] < dist[c]) dist[c] = directToComp[c]
        }
    }

    for (u in 1..compCount) {
        if (dist[u] == INF) continue
        
        var idx = dagHead[u]
        while (idx != -1) {
            val v = dagTo[idx]
            val w = dagW[idx]
            val newDist = dist[u] + w
            if (newDist < dist[v]) {
                dist[v] = newDist
            }
            idx = dagNext[idx]
        }
    }
    
    println(dist[endComp])
}
