import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val countMap = mutableMapOf<Int, Int>()
    for (num in a) {
        countMap[num] = countMap.getOrDefault(num, 0) + 1
    }
    var maxCount = 0
    var maxNum = 0
    for ((num, count) in countMap) {
        if (count > maxCount || (count == maxCount && num > maxNum)) {
            maxCount = count
            maxNum = num
        }
    }
    writer.write(maxNum.toString())
    writer.newLine()
    writer.flush()
    writer.close()
    reader.close()
}
