import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
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
    fun nextLong(): Long = next().toLong()
}

private class IntList(initialCapacity: Int = 2) {
    private var data = IntArray(initialCapacity)
    var size: Int = 0
        private set

    fun add(x: Int) {
        if (size == data.size) {
            val newData = IntArray(data.size shl 1)
            System.arraycopy(data, 0, newData, 0, data.size)
            data = newData
        }
        data[size++] = x
    }

    fun get(i: Int): Int = data[i]
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val fs = FastScanner(reader)

    val n = fs.nextInt()
    val a = LongArray(n + 1)
    for (i in 1..n) {
        a[i] = fs.nextLong()
    }

    val adj = Array(n + 1) { IntList() }
    repeat(n - 1) {
        val v = fs.nextInt()
        val u = fs.nextInt()
        adj[v].add(u)
        adj[u].add(v)
    }

    if (n == 1) {
        // Единственная площадь
        writer.write("1")
        writer.newLine()
        writer.flush()
        return
    }

    val parent = IntArray(n + 1)
    val order = IntArray(n)
    val stack = IntArray(n)
    var sp = 0
    var ordSize = 0

    // Строим порядок обхода и родителей (DFS без рекурсии, корень — 1)
    parent[1] = 0
    stack[sp++] = 1
    while (sp > 0) {
        val v = stack[--sp]
        order[ordSize++] = v
        val list = adj[v]
        for (i in 0 until list.size) {
            val to = list.get(i)
            if (to == parent[v]) continue
            parent[to] = v
            stack[sp++] = to
        }
    }

    // Подсчет сумм населения в поддеревьях
    val sub = LongArray(n + 1)
    for (i in 0 until n) {
        val v = order[i]
        sub[v] = a[v]
    }
    for (i in n - 1 downTo 0) {
        val v = order[i]
        val list = adj[v]
        for (j in 0 until list.size) {
            val to = list.get(j)
            if (to == parent[v]) continue
            sub[v] += sub[to]
        }
    }

    val total = sub[1]

    var bestNode = 1
    var bestMaxQueue = Long.MAX_VALUE

    // Для каждой площади считаем максимальную длину очереди при пункте на ней
    for (v in 1..n) {
        var maxQueue = a[v] // очередь жителей самой площади

        val list = adj[v]
        for (i in 0 until list.size) {
            val to = list.get(i)
            val sidePop = if (to == parent[v]) {
                // сторона через предка — все остальные
                total - sub[v]
            } else {
                // сторона через ребенка — его поддерево
                sub[to]
            }
            if (sidePop > maxQueue) {
                maxQueue = sidePop
            }
        }

        if (maxQueue < bestMaxQueue) {
            bestMaxQueue = maxQueue
            bestNode = v
        }
    }

    writer.write(bestNode.toString())
    writer.newLine()
    writer.flush()
}
