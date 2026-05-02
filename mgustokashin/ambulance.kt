import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Long.max

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val line = reader.readLine().split(" ")
    val k1 = line[0].toLong()
    val m = line[1].toLong()
    val k2 = line[2].toLong()
    val p2 = line[3].toLong()
    val n2 = line[4].toLong()

    if (n2 > m) {
        writer.write("-1 -1")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }
    if (k2 == 1L && (p2 != 1L || n2 != 1L)) {
         writer.write("-1 -1")
         writer.newLine()
         reader.close()
         writer.close()
         return
    }
    val minFlatPossible = (p2 - 1L) * m + (n2 - 1L) + 1L
    if (k2 < minFlatPossible) {
         writer.write("-1 -1")
         writer.newLine()
         reader.close()
         writer.close()
         return
    }

    if (m == 1L && p2 == 1L && n2 == 1L) {
        val resultP1 = if (k1 <= k2) 1L else 0L
        writer.write("$resultP1 1")
        writer.newLine()
        reader.close()
        writer.close()
        return
    }

    val possibleP1 = mutableSetOf<Long>()
    val possibleN1 = mutableSetOf<Long>()
    var validCFound = false

    val iterationLimit = 100001L
    for (c in 1L..iterationLimit) {
        val flatsPerEntrance = m * c
        if (flatsPerEntrance <= 0L) continue

        val p2Check = (k2 - 1L) / flatsPerEntrance + 1L
        val flatIndexInP2 = (k2 - 1L) % flatsPerEntrance
        val n2Check = flatIndexInP2 / c + 1L

        if (p2Check == p2 && n2Check == n2) {
            validCFound = true

            val p1Calc = (k1 - 1L) / flatsPerEntrance + 1L
            val flatIndexInP1 = (k1 - 1L) % flatsPerEntrance
            val n1Calc = flatIndexInP1 / c + 1L

            if (n1Calc > m) continue
            if (k1 == 1L && (p1Calc != 1L || n1Calc != 1L)) continue

            possibleP1.add(p1Calc)
            possibleN1.add(n1Calc)
        }
    }

    when {
        !validCFound -> writer.write("-1 -1")
        possibleP1.isEmpty() || possibleN1.isEmpty() -> writer.write("-1 -1")
        else -> {
            val resultP1 = if (possibleP1.size == 1) possibleP1.first() else 0L
            val resultN1 = if (possibleN1.size == 1) possibleN1.first() else 0L
            writer.write("$resultP1 $resultN1")
        }
    }
    writer.newLine()

    reader.close()
    writer.close()
}
