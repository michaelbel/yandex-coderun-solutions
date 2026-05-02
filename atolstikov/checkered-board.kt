import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val (nStr, kStr) = reader.readLine().split(" ")
    val n = nStr.toInt()
    val k = kStr.toInt()

    if (k == 1) {
        if (n == 1) {
            writer.write("Yes")
            writer.newLine()
            writer.write("1")
        } else {
            writer.write("No")
        }
        writer.flush()
        return
    }

    if ((n.toLong() * n) % k != 0L) {
        writer.write("No")
        writer.flush()
        return
    }

    var useDiag = false
    var blockT = 0
    var blockS = 0

    if (n % k == 0) {
        useDiag = true
    } else {
        run outer@{
            for (d in 2..k) {
                if (k % d != 0) continue
                val s = k / d
                if (s <= 1) continue
                if (n % d == 0 && n % s == 0) {
                    blockT = d
                    blockS = s
                    return@outer
                }
            }
        }
        if (blockT == 0) {
            writer.write("No")
            writer.flush()
            return
        }
    }

    writer.write("Yes")
    writer.newLine()
    val sb = StringBuilder()
    for (i in 0 until n) {
        sb.setLength(0)
        for (j in 0 until n) {
            val color = if (useDiag) {
                ( (i + j) % k ) + 1
            } else {
                ( (i % blockT) * blockS + (j % blockS) ) + 1
            }
            sb.append(color)
            if (j + 1 < n) sb.append(' ')
        }
        writer.write(sb.toString())
        writer.newLine()
    }
    writer.flush()
}
