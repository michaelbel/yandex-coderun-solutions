import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val tasks = LongArray(n)
    repeat(n) { i ->
        val line = reader.readLine().split(' ')
        val d = line[0].toInt()
        val w = line[1].toInt()
        tasks[i] = (d.toLong() shl 32) or (w.toLong() and 0xFFFFFFFFL)
    }

    tasks.sort()

    val heap = IntMinHeap(n)
    var totalWeight = 0L
    var onTimeSum = 0L

    for (enc in tasks) {
        val d = (enc shr 32).toInt()
        val w = enc.toInt()
        totalWeight += w
        heap.add(w)
        onTimeSum += w
        if (heap.size > d) {
            onTimeSum -= heap.poll()
        }
    }

    writer.write((totalWeight - onTimeSum).toString())
    writer.newLine()
    writer.flush()
}

private class IntMinHeap(capacity: Int) {
    private val heap = IntArray(capacity + 1)
    var size = 0
        private set

    fun add(x: Int) {
        var i = ++size
        heap[i] = x
        while (i > 1) {
            val p = i shr 1
            if (heap[i] < heap[p]) {
                heap[i] = heap[p].also { heap[p] = heap[i] }
                i = p
            } else break
        }
    }

    fun poll(): Int {
        val result = heap[1]
        val last = heap[size--]
        heap[1] = last
        var i = 1
        while (true) {
            val l = i shl 1
            if (l > size) break
            var smallestChild = l
            val r = l + 1
            if (r <= size && heap[r] < heap[l]) smallestChild = r
            if (heap[smallestChild] < heap[i]) {
                heap[i] = heap[smallestChild].also { heap[smallestChild] = heap[i] }
                i = smallestChild
            } else break
        }
        return result
    }
}
