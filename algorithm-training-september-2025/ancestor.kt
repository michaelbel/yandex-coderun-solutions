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
}

private class IntList(initialCapacity: Int = 1) {
    private var data = IntArray(initialCapacity)
    var size: Int = 0
        private set

    fun add(x: Int) {
        if (size == data.size) {
            val newData = IntArray(data.size * 2)
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

    val children = Array(n + 1) { IntList() }
    var root = 1

    // читаем родителей и строим список детей
    for (v in 1..n) {
        val p = fs.nextInt()
        if (p == 0) {
            root = v
        } else {
            children[p].add(v)
        }
    }

    val tin = IntArray(n + 1)
    val tout = IntArray(n + 1)
    var timer = 0

    // итеративный DFS, чтобы не ловить StackOverflow
    val stackV = IntArray(n)
    val stackIt = IntArray(n)
    var sp = 0

    stackV[sp] = root
    stackIt[sp] = 0
    sp++

    while (sp > 0) {
        val v = stackV[sp - 1]
        val it = stackIt[sp - 1]

        if (it == 0) {
            tin[v] = timer++
        }

        if (it < children[v].size) {
            val to = children[v].get(it)
            stackIt[sp - 1] = it + 1
            stackV[sp] = to
            stackIt[sp] = 0
            sp++
        } else {
            tout[v] = timer++
            sp--
        }
    }

    val m = fs.nextInt()
    val out = StringBuilder()

    repeat(m) {
        val a = fs.nextInt()
        val b = fs.nextInt()
        val isAncestor = tin[a] <= tin[b] && tout[b] <= tout[a]
        out.append(if (isAncestor) '1' else '0').append('\n')
    }

    writer.write(out.toString())
    writer.flush()
}
