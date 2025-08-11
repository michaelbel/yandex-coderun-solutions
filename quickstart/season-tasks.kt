import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun calculateTotalTasks(a: Long, b: Long): Long {
    return a + b
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val (a, b) = reader.readLine().split(" ").map { it.toLong() }
    
    val result = calculateTotalTasks(a, b)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
