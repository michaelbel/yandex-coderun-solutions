import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun isMagical(windowCounts: IntArray, sCounts: IntArray): Boolean {
    var diffs = 0
    var extraWindowChar = -1
    var extraSChar = -1
    for (i in 0 until 26) {
        if (windowCounts[i] != sCounts[i]) {
            if (windowCounts[i] == sCounts[i] + 1) {
                if (extraWindowChar != -1) return false
                extraWindowChar = i
                diffs++
            } else if (sCounts[i] == windowCounts[i] + 1) {
                if (extraSChar != -1) return false
                extraSChar = i
                diffs++
            } else {
                return false
            }
        }
    }
    return diffs == 2
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val t = reader.readLine()
    val s = reader.readLine()
    val n = t.length
    val m = s.length
    if (m == 0 || n < m) {
        writer.write("-1")
        writer.newLine()
        writer.flush()
        return
    }
    val sCounts = IntArray(26)
    val windowCounts = IntArray(26)
    for (char in s) {
        sCounts[char - 'a']++
    }
    for (i in 0 until m) {
        windowCounts[t[i] - 'a']++
    }
    if (isMagical(windowCounts, sCounts)) {
        writer.write("0")
        writer.newLine()
        writer.flush()
        return
    }
    for (i in m until n) {
        windowCounts[t[i - m] - 'a']--
        windowCounts[t[i] - 'a']++
        if (isMagical(windowCounts, sCounts)) {
            writer.write((i - m + 1).toString())
            writer.newLine()
            writer.flush()
            return
        }
    }
    writer.write("-1")
    writer.newLine()
    writer.flush()
}
