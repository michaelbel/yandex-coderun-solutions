import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun calculateTagTime(n: Int): Long {
    if (n == 1) return 1L
    if (n == 2) return 2L
    
    var t1 = 1L
    var t2 = 1L
    var sum = 2L
    
    for (i in 3..n) {
        val tNext = t1 + t2
        sum += tNext
        t1 = t2
        t2 = tNext
    }
    return sum
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val n = reader.readLine().toInt()
    
    val result = calculateTagTime(n)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
