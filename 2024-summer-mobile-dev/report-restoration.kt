import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.InputStream
import java.io.IOException
import java.util.HashMap
import java.util.ArrayList
import java.util.ArrayDeque

fun main() {
    val input = FastInput(System.`in`)
    val n = input.nextInt()
    val m = input.nextInt()
    val indexOf = HashMap<String, Int>(n)
    for (i in 0 until n) {
        val name = input.nextToken()
        indexOf[name] = i
    }
    val root = indexOf[input.nextToken()]!!
    val adj = Array(n) { ArrayList<Int>() }
    repeat(m) {
        val u = indexOf[input.nextToken()]!!
        val v = indexOf[input.nextToken()]!!
        adj[u].add(v)
    }
    val visited = BooleanArray(n)
    val queue = ArrayDeque<Int>()
    visited[root] = true
    queue.add(root)
    while (queue.isNotEmpty()) {
        val u = queue.removeFirst()
        for (v in adj[u]) {
            if (!visited[v]) {
                visited[v] = true
                queue.add(v)
            }
        }
    }
    var unreachable = 0
    for (i in 0 until n) {
        if (!visited[i]) unreachable++
    }
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    writer.write(unreachable.toString())
    writer.newLine()
    writer.flush()
}

class FastInput(private val stream: InputStream) {
    private val buf = ByteArray(1 shl 16)
    private var bufLen = 0
    private var bufPos = 0
    private fun read(): Int {
        if (bufPos >= bufLen) {
            bufPos = 0
            bufLen = stream.read(buf)
            if (bufLen == -1) return -1
        }
        return buf[bufPos++].toInt()
    }
    fun nextToken(): String {
        var c = read()
        while (c != -1 && c <= ' '.code) c = read()
        if (c == -1) throw IOException("Unexpected end of input")
        val sb = StringBuilder()
        while (c > ' '.code) {
            sb.append(c.toChar())
            c = read()
        }
        return sb.toString()
    }
    fun nextInt(): Int = nextToken().toInt()
}
