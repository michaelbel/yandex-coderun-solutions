import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val numbers = reader.readLine()
        .trim()  // Убираем лишние пробелы в начале и конце
        .split(" ")
        .filter { it.isNotEmpty() }  // Убираем пустые элементы
        .map { it.toInt() }

    var max1 = Int.MIN_VALUE
    var max2 = Int.MIN_VALUE
    var min1 = Int.MAX_VALUE
    var min2 = Int.MAX_VALUE

    for (num in numbers) {
        if (num > max1) {
            max2 = max1
            max1 = num
        } else if (num > max2) {
            max2 = num
        }

        if (num < min1) {
            min2 = min1
            min1 = num
        } else if (num < min2) {
            min2 = num
        }
    }

    if (max1.toLong() * max2 >= min1.toLong() * min2) {
        writer.write("$max2 $max1\n")
    } else {
        writer.write("$min1 $min2\n")
    }

    writer.flush()
    reader.close()
    writer.close()
}