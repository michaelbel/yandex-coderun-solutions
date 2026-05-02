import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer

private class IntList(initialCapacity: Int = 2) {
    private var data = IntArray(initialCapacity)
    var size: Int = 0
        private set

    fun add(x: Int) {
        if (size == data.size) {
            data = data.copyOf(data.size shl 1)
        }
        data[size++] = x
    }

    fun get(i: Int): Int = data[i]
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().trim().toInt()

    val adj = Array(n + 1) { IntList() }
    val degree = IntArray(n + 1)

    repeat(n - 1) {
        val st = StringTokenizer(reader.readLine())
        val a = st.nextToken().toInt()
        val b = st.nextToken().toInt()
        adj[a].add(b)
        adj[b].add(a)
        degree[a]++
        degree[b]++
    }

    // Список листьев
    val leaves = IntArray(n)
    var leafCount = 0
    for (v in 1..n) {
        if (degree[v] == 1 || (n == 1)) { // на дереве из 2+ вершин листьев минимум два
            leaves[leafCount++] = v
        }
    }

    // Мульти-источниковый BFS от всех листьев
    val dist = IntArray(n + 1) { -1 }
    val src = IntArray(n + 1) { -1 }
    val queue = IntArray(n + 1)
    var head = 0
    var tail = 0

    for (i in 0 until leafCount) {
        val v = leaves[i]
        dist[v] = 0
        src[v] = v
        queue[tail++] = v
    }

    var answer = Int.MAX_VALUE

    while (head < tail) {
        val v = queue[head++]
        val dv = dist[v]
        val sv = src[v]

        val list = adj[v]
        for (i in 0 until list.size) {
            val to = list.get(i)
            if (dist[to] == -1) {
                dist[to] = dv + 1
                src[to] = sv
                queue[tail++] = to
            } else if (src[to] != sv) {
                // Волны от двух разных листьев встретились
                val candidate = dist[to] + dv + 1
                if (candidate < answer) {
                    answer = candidate
                }
            }
        }
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
