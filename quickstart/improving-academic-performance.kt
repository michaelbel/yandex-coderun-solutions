import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun improvingAcademicPerformance(count2: Long, count3: Long, count4: Long): Long {
    val target = 4
    val maxValue = 10000000000000000L
    
    var left = 0L
    var right = maxValue

    while (left < right) {
        val medium = (left + right) / 2
        if (checkMark(count2, count3, count4, medium, target)) {
            right = medium
        } else {
            left = medium + 1
        }
    }

    return left
}

private fun checkMark(count2: Long, count3: Long, count4: Long, count5: Long, target: Int): Boolean {
    return 2 * (count2 * 2 + count3 * 3 + count4 * 4 + count5 * 5) >= (target * 2 - 1) * (count2 + count3 + count4 + count5)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val count2 = reader.readLine().toLong()
    val count3 = reader.readLine().toLong()
    val count4 = reader.readLine().toLong()

    val res = improvingAcademicPerformance(count2, count3, count4)
    writer.write(res.toString())
    writer.newLine()

    reader.close()
    writer.close()
}
