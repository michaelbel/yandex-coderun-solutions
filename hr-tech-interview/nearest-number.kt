import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import kotlin.math.abs

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt() // Читаем количество чисел в массиве
    val numbers = reader.readLine().split(" ").map(String::toInt) // Читаем массив чисел
    val x = reader.readLine().toInt() // Читаем целевое число

    var closest = numbers[0] // Инициализируем ближайшее число первым элементом массива
    var minDiff = abs(numbers[0] - x) // Разница между первым числом и целевым

    for (num in numbers) {
        val diff = abs(num - x) // Вычисляем разницу с целевым числом
        if (diff < minDiff) { // Если разница меньше текущей минимальной
            closest = num // Обновляем ближайшее число
            minDiff = diff // Обновляем минимальную разницу
        }
    }

    writer.write(closest.toString()) // Выводим ближайшее число

    reader.close()
    writer.flush()
    writer.close()
}