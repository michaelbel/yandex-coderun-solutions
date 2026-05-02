import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun charToIndex(c: Char): Int {
    return if (c in 'a'..'z') c - 'a' else c - 'A' + 26
}

fun arraysEqual(a: IntArray, b: IntArray): Boolean {
    for (i in a.indices) {
        if (a[i] != b[i]) return false
    }
    return true
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (g, sLen) = reader.readLine().split(" ").map { it.toInt() }
    val w = reader.readLine()
    val s = reader.readLine()

    val wFreq = IntArray(52) { 0 }
    for (c in w) {
        wFreq[charToIndex(c)]++
    }

    val sFreq = IntArray(52) { 0 }
    for (i in 0 until g) {
        sFreq[charToIndex(s[i])]++
    }

    var count = if (arraysEqual(wFreq, sFreq)) 1 else 0

    for (i in g until sLen) {
        sFreq[charToIndex(s[i - g])]--
        sFreq[charToIndex(s[i])]++
        if (arraysEqual(wFreq, sFreq)) count++
    }

    writer.write(count.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
