import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val (a, b) = reader.readLine().split(" ").map { it.toLong() }
    
    val gcdResult = gcd(a, b)
    val lcmResult = lcm(a, b)
    
    writer.write("$gcdResult $lcmResult")
    writer.newLine()
    
    reader.close()
    writer.close()
}
