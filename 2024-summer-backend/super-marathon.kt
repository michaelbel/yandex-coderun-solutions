import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

fun extGcd(a: Long, b: Long): LongArray {
    if (b == 0L) return longArrayOf(a, 1, 0)
    val res = extGcd(b, a % b)
    val g = res[0]
    val x1 = res[1]
    val y1 = res[2]
    val x = y1
    val y = x1 - (a / b) * y1
    return longArrayOf(g, x, y)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val parseTime = { s: String ->
        val (hh, mm) = s.split(':')
        hh.toLong() * 60 + mm.toLong()
    }

    val startA = parseTime(reader.readLine().trim())
    val startB = parseTime(reader.readLine().trim())
    val periodA = parseTime(reader.readLine().trim())
    val periodB = parseTime(reader.readLine().trim())

    val g = gcd(periodA, periodB)
    val delta = startB - startA
    if (delta % g != 0L) {
        writer.write("Never\n")
        writer.flush()
        return
    }

    val m1 = periodA
    val m2 = periodB
    val m1Div = m1 / g
    val m2Div = m2 / g
    val dDiv = delta / g

    val eg = extGcd(m1Div, m2Div)
    val invM1 = ((eg[1] % m2Div) + m2Div) % m2Div

    val k0 = ((dDiv % m2Div) + m2Div) % m2Div
    val k = (k0 * invM1) % m2Div

    val x0 = startA + m1 * k
    val lcm = m1Div * m2

    val x0Mod = ((x0 % lcm) + lcm) % lcm

    val earliest = maxOf(startA + periodA, startB + periodB)

    val T = if (x0Mod >= earliest) {
        x0Mod
    } else {
        val diff = earliest - x0Mod
        val mult = (diff + lcm - 1) / lcm
        x0Mod + mult * lcm
    }

    val days = arrayOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
    val daysOffset = (T / 1440) % 7
    val dayIdx = (6 + daysOffset.toInt()) % 7
    val timeOfDay = (T % 1440).toInt()
    val hh = timeOfDay / 60
    val mm = timeOfDay % 60
    val timeStr = String.format("%02d:%02d", hh, mm)

    writer.write(days[dayIdx])
    writer.newLine()
    writer.write(timeStr)
    writer.newLine()
    writer.flush()
}
