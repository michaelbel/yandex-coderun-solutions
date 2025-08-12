import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val s = reader.readLine()
    val cleaned = s.filter { it != ' ' }.lowercase()
    var isPalindrome = true
    for (i in 0 until cleaned.length / 2) {
        if (cleaned[i] != cleaned[cleaned.length - 1 - i]) {
            isPalindrome = false
            break
        }
    }
    if (isPalindrome) {
        writer.write("It is a palindrome")
    } else {
        writer.write("It is not a palindrome")
    }
    writer.newLine()
    reader.close()
    writer.close()
}
