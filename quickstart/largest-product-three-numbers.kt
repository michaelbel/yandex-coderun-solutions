import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val numbers = reader.readLine().split(" ").map { it.toLong() }
    val sorted = numbers.sorted()
    val n = sorted.size
    
    val prod1 = sorted[n-1] * sorted[n-2] * sorted[n-3]
    val prod2 = sorted[0] * sorted[1] * sorted[n-1]
    
    val result = if (prod1 > prod2) {
        listOf(sorted[n-3], sorted[n-2], sorted[n-1])
    } else {
        listOf(sorted[0], sorted[1], sorted[n-1])
    }
    
    writer.write(result.joinToString(" "))
    
    reader.close()
    writer.close()
}
