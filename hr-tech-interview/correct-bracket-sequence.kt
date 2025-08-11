import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Stack

fun matches(open: Char, close: Char): Boolean =
    open == '(' && close == ')' ||
            open == '[' && close == ']' ||
            open == '{' && close == '}'

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine()
    val stack = Stack<Char>()

    for (c in s) {
        when (c) {
            '(', '[', '{' -> stack.push(c)
            ')', ']', '}' -> {
                if (stack.isEmpty() || !matches(stack.pop(), c)) {
                    writer.write("no")
                    writer.flush()
                    return
                }
            }
        }
    }

    if (stack.isEmpty()) writer.write("yes")
    else writer.write("no")

    writer.flush()
}