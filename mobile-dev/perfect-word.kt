import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.min

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val k = reader.readLine().trim().toInt()
    val s = reader.readLine().trim()
    val n = s.length
    val d1 = IntArray(n)
    var l = 0
    var r = -1
    for (i in 0 until n) {
        var kLocal = if (i > r) 1 else min(d1[l + r - i], r - i + 1)
        while (i - kLocal >= 0 && i + kLocal < n && s[i - kLocal] == s[i + kLocal]) {
            kLocal++
        }
        d1[i] = kLocal
        kLocal--
        if (i + kLocal > r) {
            l = i - kLocal
            r = i + kLocal
        }
    }
    val d2 = IntArray(n)
    l = 0
    r = -1
    for (i in 0 until n) {
        var kLocal = if (i > r) 0 else min(d2[l + r - i + 1], r - i + 1)
        while (i - kLocal - 1 >= 0 && i + kLocal < n && s[i - kLocal - 1] == s[i + kLocal]) {
            kLocal++
        }
        d2[i] = kLocal
        if (i + kLocal - 1 > r) {
            l = i - kLocal
            r = i + kLocal - 1
        }
    }
    fun check(p: Int): Boolean {
        var cnt = 0
        var i = 0
        while (i <= n - p) {
            if (p and 1 == 1) {
                val mid = i + p / 2
                if (d1[mid] >= (p / 2 + 1)) {
                    cnt++
                    i += p
                    if (cnt >= k) return true
                    continue
                }
            } else {
                val mid = i + p / 2
                if (d2[mid] >= (p / 2)) {
                    cnt++
                    i += p
                    if (cnt >= k) return true
                    continue
                }
            }
            i++
        }
        return false
    }
    var ans = 0
    for (p in n downTo 1) {
        if (check(p)) {
            ans = p
            break
        }
    }
    writer.write(ans.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
