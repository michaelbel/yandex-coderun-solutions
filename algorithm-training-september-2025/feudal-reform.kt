import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.math.abs

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

    // строим дерево подчинения: список детей для каждой вершины
    val children = Array(n) { IntList() }
    // вершины нумеруются от 0, корень — 0
    for (v in 1 until n) {
        val p = fs.nextInt()
        children[p].add(v)
    }

    val a = LongArray(n)
    for (i in 0 until n) {
        a[i] = fs.nextLong()
    }

    // получаем порядок обхода (DFS) и потом обрабатываем в обратном порядке (постфикс)
    val order = IntArray(n)
    val stack = IntArray(n)
    var sp = 0
    var ordSize = 0

    stack[sp++] = 0
    while (sp > 0) {
        val v = stack[--sp]
        order[ordSize++] = v
        val list = children[v]
        for (i in 0 until list.size) {
            stack[sp++] = list.get(i)
        }
    }

    var answer = 0L

    // x[v] нам не нужен целиком, достаточно по ходу считать |x[v]| и добавлять к answer
    for (idx in n - 1 downTo 0) {
        val v = order[idx]
        var sumChildrenA = 0L
        val list = children[v]
        for (i in 0 until list.size) {
            val c = list.get(i)
            sumChildrenA += a[c]
        }
        val x = sumChildrenA - a[v]
        answer += abs(x)
        // значения a[] не меняем, они исходные и нужны только для формулы
    }

    writer.write(answer.toString())
    writer.newLine()
    writer.flush()
}
