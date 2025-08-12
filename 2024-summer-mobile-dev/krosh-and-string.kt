import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun canRemoveAll(s: String): Int {
    val stack = mutableListOf<Char>()
    for (c in s) {
        if (stack.isNotEmpty() && stack.last() == c) {
            stack.removeAt(stack.size - 1)
        } else {
            stack.add(c)
        }
    }
    return if (stack.isEmpty()) 1 else 0
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val s = reader.readLine()
    val result = canRemoveAll(s)
    writer.write(result.toString())
    writer.newLine()
    reader.close()
    writer.close()
}
