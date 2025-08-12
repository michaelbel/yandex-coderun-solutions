import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun upgradeYourHero(nums: IntArray): Int {
    val negCount = nums.count { it < 0 }
    val zeroCount = nums.count { it == 0 }
    val posCount = nums.size - negCount - zeroCount
    return when {
        zeroCount > 1 -> 0
        zeroCount == 1 -> if (negCount % 2 == 0) 0 else nums.min()
        negCount % 2 == 0 ->
            if (posCount != 0) nums.minBy { if (it < 0) Int.MAX_VALUE else it }
            else nums.min()
        else -> nums.maxBy { if (it > 0) Int.MIN_VALUE else it }
    }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    reader.readLine()
    val nums = reader.readLine().split(" ").map { it.toInt() }.toIntArray()
    val res = upgradeYourHero(nums)
    writer.write(res.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
