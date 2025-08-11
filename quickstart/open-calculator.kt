import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val buttons = reader.readLine().trim().split(" ").map { it.trim().first() }.toSet()
    val N = reader.readLine().trim()
    val digitsInN = N.map { it }.toSet()
    val missingDigits = digitsInN - buttons
    writer.write("${missingDigits.size}\n")
    
    writer.flush()
    writer.close()
    reader.close()
}
