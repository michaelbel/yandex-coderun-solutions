import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val letterCount = mutableMapOf<Char, Int>()
    repeat(n) {
        val command = reader.readLine()
        if (command.isNotEmpty()) {
            val firstLetter = command[0]
            letterCount[firstLetter] = letterCount.getOrDefault(firstLetter, 0) + 1
        }
    }
    val result = letterCount.maxByOrNull { it.value }?.key ?: 'a'
    writer.write(result.toString())
    reader.close()
    writer.close()
}
