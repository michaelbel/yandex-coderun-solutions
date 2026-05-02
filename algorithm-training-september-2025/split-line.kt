import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import kotlin.system.exitProcess

private class IntStack(initialCapacity: Int = 1) {
    private var data = IntArray(initialCapacity)
    var size: Int = 0
        private set

    fun push(value: Int) {
        if (size == data.size) {
            val newData = IntArray(data.size * 2)
            System.arraycopy(data, 0, newData, 0, data.size)
            data = newData
        }
        data[size++] = value
    }

    fun pop(): Int {
        if (size == 0) throw IllegalStateException("Pop from empty stack")
        size--
        return data[size]
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine() ?: run {
        writer.flush()
        exitProcess(0)
    }
    val st = StringTokenizer(firstLine)
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()

    val s = reader.readLine() ?: ""
    val len = n / m

    // Map from piece string to stack of its indices (1-based)
    val map = HashMap<String, IntStack>(m * 2)

    for (i in 1..m) {
        val piece = reader.readLine() ?: ""
        val stack = map.getOrPut(piece) { IntStack() }
        stack.push(i)
    }

    val result = IntArray(m)

    var pos = 0
    for (i in 0 until m) {
        val part = s.substring(pos, pos + len)
        pos += len

        val stack = map[part]
        if (stack == null || stack.size == 0) {
            // По условию такого быть не должно, но на всякий случай
            writer.flush()
            exitProcess(0)
        }
        result[i] = stack.pop()
    }

    val sb = StringBuilder()
    for (i in 0 until m) {
        if (i > 0) sb.append(' ')
        sb.append(result[i])
    }
    sb.append(' ')

    writer.write(sb.toString())
    writer.newLine()
    writer.flush()
}
