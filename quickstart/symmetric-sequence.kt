import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private fun symmetricSequence(nums: IntArray): IntArray {
    val seq = mutableListOf(*nums.toTypedArray())
    val size = nums.size
    var count = 0
    while (!isPalindromic(seq)) {
        seq.add(size, nums[count])
        count++
    }
    return seq.subList(nums.size, seq.size).toIntArray()
}

private fun isPalindromic(list: List<Int>) = list == list.reversed()

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    reader.readLine()
    val nums = reader.readLine().trim().split(" ").map { it.toInt() }.toIntArray()

    val res = symmetricSequence(nums)
    writer.write("${res.size}\n")
    writer.write(res.joinToString(" ") + "\n")

    reader.close()
    writer.close()
}
