import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun countBalls(k: Int): Long {
    return (1L shl k) - 1
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val k = reader.readLine().toInt()
    
    val result = countBalls(k)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
