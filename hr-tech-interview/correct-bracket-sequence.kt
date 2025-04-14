import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Stack

// Функция проверки соответствия открывающей и закрывающей скобок
fun matches(open: Char, close: Char): Boolean =
    open == '(' && close == ')' ||
            open == '[' && close == ']' ||
            open == '{' && close == '}'

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val s = reader.readLine()
    val stack = Stack<Char>()

    // Проходим по каждому символу в строке
    for (c in s) {
        when (c) {
            // Если скобка открывающая, добавляем её в стек
            '(', '[', '{' -> stack.push(c)

            // Если скобка закрывающая, проверяем соответствие
            ')', ']', '}' -> {
                if (stack.isEmpty() || !matches(stack.pop(), c)) {
                    writer.write("no")
                    writer.flush()
                    return
                }
            }
        }
    }

    // Проверяем, пуст ли стек (все скобки корректно закрыты)
    if (stack.isEmpty()) writer.write("yes")
    else writer.write("no")

    writer.flush()
}