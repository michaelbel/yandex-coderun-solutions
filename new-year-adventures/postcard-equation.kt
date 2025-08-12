import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun primeDivisorsCount(x: Long): Int {
    var num = x
    var count = 0
    if (num % 2 == 0L) {
        count++
        while (num % 2 == 0L) num /= 2
    }
    var i = 3L
    while (i * i <= num) {
        if (num % i == 0L) {
            count++
            while (num % i == 0L) num /= i
        }
        i += 2
    }
    if (num > 1) count++
    return count
}

fun countSolutions(n: Long): Int {
    var count = 0
    for (k in 0..60) {
        val x = n - k
        if (x <= 0) break
        if (primeDivisorsCount(x) == k) count++
    }
    return count
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val n = reader.readLine().toLong()
    
    val result = countSolutions(n)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
