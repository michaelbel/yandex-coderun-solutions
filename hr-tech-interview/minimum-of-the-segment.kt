import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Считываем весь вход одним вызовом и разбиваем по пробельным символам
    val input = reader.readText().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    var index = 0
    val n = input[index++].toInt()
    val k = input[index++].toInt()

    // Считываем последовательность из n чисел
    val arr = IntArray(n) { input[index++].toInt() }

    // Дек для хранения индексов элементов, который будет поддерживать значения в возрастающем порядке.
    val deque = ArrayDeque<Int>(n)
    val sb = StringBuilder()

    for (i in 0 until n) {
        // Удаляем из дека индексы, вышедшие за левую границу текущего окна
        while (deque.isNotEmpty() && deque.first() <= i - k) {
            deque.removeFirst()
        }

        // Удаляем из конца дека все индексы, для которых значение элемента больше или равно текущему
        while (deque.isNotEmpty() && arr[deque.last()] >= arr[i]) {
            deque.removeLast()
        }

        // Добавляем текущий индекс
        deque.addLast(i)

        // Если окно полностью сформировано, добавляем в ответ минимум окна (элемент с индексом deque.first())
        if (i >= k - 1) {
            sb.append(arr[deque.first()]).append("\n")
        }
    }

    writer.write(sb.toString())
    writer.flush()

    reader.close()
    writer.close()
}