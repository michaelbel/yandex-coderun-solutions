import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.TreeMap

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (n, k, r) = reader.readLine().split(" ").map { it.toInt() }
    val aParts = reader.readLine().split(" ")
    val a = LongArray(n) { i -> aParts[i].toLong() }

    val smallMap = TreeMap<Long, Int>()
    val largeMap = TreeMap<Long, Int>()
    var sumSmall = 0L

    for (i in 0 until k) {
        val x = a[i]
        smallMap[x] = smallMap.getOrDefault(x, 0) + 1
        sumSmall += x
    }
    for (i in k until n) {
        val x = a[i]
        largeMap[x] = largeMap.getOrDefault(x, 0) + 1
    }

    repeat(r) {
        val S = sumSmall
        val xEntry = smallMap.firstEntry()
        val x = xEntry.key
        if (xEntry.value == 1) smallMap.remove(x) else smallMap[x] = xEntry.value - 1
        sumSmall -= x

        largeMap[S] = largeMap.getOrDefault(S, 0) + 1

        val yEntry = largeMap.firstEntry()
        val y = yEntry.key
        if (yEntry.value == 1) largeMap.remove(y) else largeMap[y] = yEntry.value - 1
        smallMap[y] = smallMap.getOrDefault(y, 0) + 1
        sumSmall += y
    }

    val sb = StringBuilder()
    for ((value, count) in smallMap) {
        repeat(count) { sb.append(value).append(' ') }
    }
    for ((value, count) in largeMap) {
        repeat(count) { sb.append(value).append(' ') }
    }
    if (sb.isNotEmpty()) sb.setLength(sb.length - 1)
    writer.write(sb.toString())
    writer.newLine()
    writer.flush()
}
