import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun countUniqueElements(arr: List<Int>): Int {
    val frequency = mutableMapOf<Int, Int>()
    for (num in arr) {
        frequency[num] = frequency.getOrDefault(num, 0) + 1
    }
    return frequency.values.count { it == 1 }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    reader.readLine()
    val arr = reader.readLine().split(" ").map(String::toInt)
    writer.write(countUniqueElements(arr).toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
