import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

data class IntPoint(val x: Int, val y: Int)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val h = Array(4) { IntPoint(0, 0) }
    for (i in 0 until 4) {
        val parts = reader.readLine().split(" ")
        h[i] = IntPoint(parts[0].toInt(), parts[1].toInt())
    }

    val numA_x = h[1].x + h[2].x + h[3].x - 2 * h[0].x
    val numA_y = h[1].y + h[2].y + h[3].y - 2 * h[0].y

    val numB_x = h[0].x + h[2].x + h[3].x - 2 * h[1].x
    val numB_y = h[0].y + h[2].y + h[3].y - 2 * h[1].y

    val numC_x = h[0].x + h[1].x + h[3].x - 2 * h[2].x
    val numC_y = h[0].y + h[1].y + h[3].y - 2 * h[2].y

    val numD_x = h[0].x + h[1].x + h[2].x - 2 * h[3].x
    val numD_y = h[0].y + h[1].y + h[2].y - 2 * h[3].y

    val sqNumA = numA_x.toLong() * numA_x + numA_y.toLong() * numA_y
    val sqNumB = numB_x.toLong() * numB_x + numB_y.toLong() * numB_y
    val sqNumC = numC_x.toLong() * numC_x + numC_y.toLong() * numC_y
    val sqNumD = numD_x.toLong() * numD_x + numD_y.toLong() * numD_y

    if (sqNumA == sqNumB && sqNumA == sqNumC && sqNumA == sqNumD && sqNumA > 0) {
        writer.write("YES\n")
        val three = 3.0
        writer.write("%.12f %.12f\n".format(numA_x / three, numA_y / three))
        writer.write("%.12f %.12f\n".format(numB_x / three, numB_y / three))
        writer.write("%.12f %.12f\n".format(numC_x / three, numC_y / three))
        writer.write("%.12f %.12f\n".format(numD_x / three, numD_y / three))
    } else {
        writer.write("NO\n")
    }

    reader.close()
    writer.close()
}
