import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.HashMap
import java.util.StringTokenizer

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.`out`))
    val st0 = StringTokenizer(reader.readLine())
    val K = st0.nextToken().toInt()
    val N = st0.nextToken().toInt()
    val maxNodes = 2 * N + 5
    val nodePrev = IntArray(maxNodes)
    val nodeValue = IntArray(maxNodes)
    val map = HashMap<Long, Int>(maxNodes)
    var nodeCount = 1
    val cur = IntArray(K)
    val size = IntArray(K)
    val visited = BooleanArray(maxNodes)
    var distinctCount = 0

    repeat(N) {
        val st = StringTokenizer(reader.readLine())
        when (st.nextToken().toInt()) {
            1 -> {
                val idx = st.nextToken().toInt() - 1
                val x = st.nextToken().toInt()
                val prev = cur[idx]
                val key = (x.toLong() shl 32) or (prev.toLong() and 0xffffffffL)
                val id = map.getOrPut(key) {
                    nodePrev[nodeCount] = prev
                    nodeValue[nodeCount] = x
                    nodeCount++
                }
                cur[idx] = id
                size[idx]++
                if (!visited[id]) {
                    visited[id] = true
                    distinctCount++
                }
            }
            2 -> {
                val idx = st.nextToken().toInt() - 1
                val prevId = cur[idx]
                val newId = nodePrev[prevId]
                cur[idx] = newId
                size[idx]--
                if (newId != 0 && !visited[newId]) {
                    visited[newId] = true
                    distinctCount++
                }
            }
            3 -> {
                var i1 = st.nextToken().toInt() - 1
                var i2 = st.nextToken().toInt() - 1
                if (size[i1] < size[i2]) {
                    val tmp = i1
                    i1 = i2
                    i2 = tmp
                }
                while (size[i1] - size[i2] > 1) {
                    val prevBig = cur[i1]
                    val newBig = nodePrev[prevBig]
                    cur[i1] = newBig
                    size[i1]--
                    if (newBig != 0 && !visited[newBig]) {
                        visited[newBig] = true
                        distinctCount++
                    }
                    val xVal = nodeValue[prevBig]
                    val prevSmall = cur[i2]
                    val key2 = (xVal.toLong() shl 32) or (prevSmall.toLong() and 0xffffffffL)
                    val id2 = map.getOrPut(key2) {
                        nodePrev[nodeCount] = prevSmall
                        nodeValue[nodeCount] = xVal
                        nodeCount++
                    }
                    cur[i2] = id2
                    size[i2]++
                    if (!visited[id2]) {
                        visited[id2] = true
                        distinctCount++
                    }
                }
            }
        }
    }

    writer.write(distinctCount.toString())
    writer.newLine()
    writer.flush()
}
