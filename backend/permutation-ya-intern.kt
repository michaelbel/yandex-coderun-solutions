import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.IllegalArgumentException

fun longSqrt(n: Long): Long {
    if (n < 0) throw IllegalArgumentException("negative")
    if (n == 0L) return 0L
    var low = 1L
    var high = 3_000_000_000L
    var ans = 0L
    while (low <= high) {
        val mid = low + (high - low) / 2
        if (mid > Long.MAX_VALUE / mid) {
            high = mid - 1
            continue
        }
        val midSquared = mid * mid
        if (midSquared <= n) {
            ans = mid
            low = mid + 1
        } else {
            high = mid - 1
        }
    }
    return ans
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toLong()
    val parts = reader.readLine().split(" ")
    val s1p = parts[0].toLong()
    val s2p = parts[1].toLong()
    val s3p = parts[2].toLong()
    val ts1 = n * (n + 1) / 2
    val ts2 = n * (n + 1) * (2 * n + 1) / 6
    val ts3 = ts1 * ts1
    val s1 = ts1 - s1p
    val s2 = ts2 - s2p
    val s3 = ts3 - s3p
    val e1 = s1
    val e2 = (s1 * s1 - s2) / 2
    val e3 = (s1 * s1 * s1 - 3 * s1 * s2 + 2 * s3) / 6
    for (xL in 1L..n) {
        val xSquared = xL * xL
        val xCubed = xSquared * xL
        if (xCubed - e1 * xSquared + e2 * xL - e3 == 0L) {
            val sumYZ = e1 - xL
            val prodYZ = e2 - e1 * xL + xSquared
            val discriminant = sumYZ * sumYZ - 4L * prodYZ
            if (discriminant >= 0L) {
                val sqrtD = longSqrt(discriminant)
                if (sqrtD * sqrtD == discriminant) {
                    val yLongNum = sumYZ + sqrtD
                    val zLongNum = sumYZ - sqrtD
                    if (yLongNum % 2L == 0L && zLongNum % 2L == 0L) {
                        val yL = yLongNum / 2L
                        val zL = zLongNum / 2L
                        if (yL in 1L..n && zL in 1L..n) {
                            if (xL != yL && xL != zL && yL != zL) {
                                writer.write("$xL $yL $zL\n")
                                reader.close()
                                writer.flush()
                                writer.close()
                                return
                            }
                        }
                    }
                }
            }
        }
    }
    reader.close()
    writer.flush()
    writer.close()
}
