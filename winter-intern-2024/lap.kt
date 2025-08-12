import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Long
import java.lang.Integer

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val firstLine = reader.readLine().split(" ").map { Integer.parseInt(it) }
    val n = firstLine[0]
    val t = firstLine[1]
    val s = firstLine[2]

    val speeds = reader.readLine().split(" ").map { Integer.parseInt(it) }

    val v1 = speeds[0]
    var totalOvertakes = 0L
    val tLong = t.toLong()
    val sLong = s.toLong()
    val v1Long = v1.toLong()

    for (i in 1 until n) {
        val vi = speeds[i]
        val viLong = vi.toLong()

        if (v1 > vi) {
            val diffDist = (v1Long - viLong) * tLong
            val lapsGained = diffDist / sLong

            if (diffDist > 0L && diffDist % sLong == 0L) {
                totalOvertakes += lapsGained - 1
            } else {
                totalOvertakes += lapsGained
            }
        }
    }

    writer.write(totalOvertakes.toString())
    writer.flush()
    reader.close()
    writer.close()
}
