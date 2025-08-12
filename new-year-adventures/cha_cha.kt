import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun calculateFinalGrade(s: String): Char {
    val grades = s.map { 'Z' - it + 1 }
    val avg = grades.sum().toDouble() / grades.size
    val rawGrade = 'Z' - (avg + 0.5).toInt() + 1
    val minGrade = s.maxOrNull()!! - 1
    return maxOf(rawGrade, minGrade)
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    
    val s = reader.readLine()
    
    val result = calculateFinalGrade(s)
    writer.write(result.toString())
    writer.newLine()
    
    reader.close()
    writer.close()
}
