import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val productToCategory = mutableMapOf<Int, Int>()
    repeat(n) {
        val (productId, category) = reader.readLine().split(" ").map { it.toInt() }
        productToCategory[productId] = category
    }
    val order = reader.readLine().split(" ").map { it.toInt() }
    val lastPosition = mutableMapOf<Int, Int>()
    var minDistance = n
    for (i in order.indices) {
        val category = productToCategory[order[i]]!!
        if (category in lastPosition) {
            val distance = i - lastPosition[category]!!
            if (distance < minDistance) minDistance = distance
        }
        lastPosition[category] = i
    }
    writer.write(minDistance.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
