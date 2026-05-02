import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.abs

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (n, m, k) = reader.readLine().split(' ').map { it.toInt() }
    val wishes = Array(n) { ArrayList<Int>() }
    repeat(k) {
        val parts = reader.readLine().split(' ')
        val a = parts[0].toInt() - 1
        val b = parts[1].toInt()
        val v = abs(b) - 1
        val lit = if (b > 0) 2 * v else 2 * v + 1
        wishes[a].add(lit)
    }
    var totalAux = 0
    for (i in 0 until n) {
        val sz = wishes[i].size
        if (sz >= 2) totalAux += sz - 1
    }
    val totalVars = m + totalAux
    val N = totalVars * 2
    val graph = Array<MutableList<Int>>(N) { ArrayList() }
    val revGraph = Array<MutableList<Int>>(N) { ArrayList() }

    fun addImp(u: Int, v: Int) {
        graph[u].add(v)
        revGraph[v].add(u)
    }
    fun clause(litA: Int, litB: Int) {
        val notA = litA xor 1
        val notB = litB xor 1
        addImp(notA, litB)
        addImp(notB, litA)
    }

    var nextAux = m
    for (i in 0 until n) {
        val lst = wishes[i]
        val r = lst.size
        if (r >= 2) {
            val aux = IntArray(r - 1) { j -> nextAux + j }
            nextAux += r - 1
            for (j in 0 until r - 1) {
                val litj = lst[j]
                val sj = aux[j]
                clause(litj, 2 * sj)
                clause(lst[j + 1], 2 * sj + 1)
            }
            for (j in 1 until r - 1) {
                val sprev = aux[j - 1]
                val sj = aux[j]
                clause(2 * sprev + 1, 2 * sj)
            }
        }
    }

    val visited = BooleanArray(N)
    val order = IntArray(N)
    var op = 0
    fun dfs1(u: Int) {
        visited[u] = true
        for (v in graph[u]) if (!visited[v]) dfs1(v)
        order[op++] = u
    }
    for (u in 0 until N) if (!visited[u]) dfs1(u)

    val comp = IntArray(N) { -1 }
    var cid = 0
    fun dfs2(u: Int) {
        comp[u] = cid
        for (v in revGraph[u]) if (comp[v] == -1) dfs2(v)
    }
    for (idx in N - 1 downTo 0) {
        val u = order[idx]
        if (comp[u] == -1) {
            dfs2(u)
            cid++
        }
    }

    val assign = BooleanArray(totalVars)
    for (v in 0 until totalVars) {
        if (comp[2 * v] == comp[2 * v + 1]) {
            writer.write("-1")
            writer.newLine()
            writer.flush()
            return
        }
        assign[v] = comp[2 * v] > comp[2 * v + 1]
    }

    val result = ArrayList<Int>()
    for (i in 0 until m) if (assign[i]) result.add(i + 1)
    writer.write(result.size.toString())
    writer.newLine()
    if (result.isNotEmpty()) {
        writer.write(result.joinToString(" "))
        writer.newLine()
    }
    writer.flush()
}
