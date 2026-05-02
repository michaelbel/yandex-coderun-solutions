import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем входные данные
    val (n, k) = reader.readLine().split(" ").map { it.toInt() }
    val stops = reader.readLine().split(" ").map { it.toLong() }
    val queries = reader.readLine().split(" ").map { it.toLong() }

    // Обрабатываем каждый запрос
    for (x in queries) {
        // Бинарный поиск ближайшей остановки
        var left = 0
        var right = n - 1
        
        while (left <= right) {
            val mid = (left + right) / 2
            if (stops[mid] == x) {
                // Если нашли точное совпадение, ищем минимальный индекс
                var result = mid
                while (result > 0 && stops[result - 1] == x) {
                    result--
                }
                writer.write("${result + 1}\n")
                break
            } else if (stops[mid] < x) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
        
        // Если точного совпадения нет
        if (left > right) {
            val leftIndex = right  // последняя остановка слева
            val rightIndex = left  // первая остановка справа
            
            when {
                // Нет остановок слева
                leftIndex < 0 -> writer.write("${rightIndex + 1}\n")
                // Нет остановок справа
                rightIndex >= n -> writer.write("${leftIndex + 1}\n")
                else -> {
                    val leftDist = x - stops[leftIndex]
                    val rightDist = stops[rightIndex] - x
                    
                    when {
                        // Одинаковое расстояние до левой и правой
                        leftDist == rightDist -> writer.write("${leftIndex + 1}\n")
                        // Ближе левая
                        leftDist < rightDist -> writer.write("${leftIndex + 1}\n")
                        // Ближе правая
                        else -> writer.write("${rightIndex + 1}\n")
                    }
                }
            }
        }
    }

    reader.close()
    writer.close()
}
