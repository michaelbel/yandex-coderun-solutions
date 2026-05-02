import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val parts = reader.readLine().trim().split("\\s+".toRegex())
    val a = IntArray(4) { parts[it].toInt() }

    var maxTruth = 0
    for (x0 in 0..4) {
        for (x1 in 0..4) {
            for (x2 in 0..4) {
                for (x3 in 0..4) {
                    val sum = x0 + x1 + x2 + x3
                    if (sum in 2..4) {
                        var truth = 0
                        if (a[0] == x0) truth++
                        if (a[1] == x1) truth++
                        if (a[2] == x2) truth++
                        if (a[3] == x3) truth++
                        if (truth > maxTruth) {
                            maxTruth = truth
                        }
                    }
                }
            }
        }
    }

    val liars = 4 - maxTruth
    writer.write(liars.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
