import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем количество элементов в массиве (n) и целевую сумму (k)
    val (n, k) = reader.readLine().split(" ").map { it.toInt() }
    // Читаем массив чисел
    val arr = reader.readLine().split(" ").map { it.toInt() }

    var currentSum = 0L // Текущая сумма префикса
    var count = 0 // Количество подмассивов с суммой k
    val prefixSumCount = mutableMapOf<Long, Int>() // Хранение количества встреченных сумм префиксов
    prefixSumCount[0L] = 1 // Начальная сумма 0 встречается 1 раз

    for (num in arr) {
        currentSum += num.toLong() // Обновляем текущую сумму префикса
        val target = currentSum - k.toLong() // Вычисляем разницу с целевой суммой
        count += prefixSumCount.getOrDefault(target, 0) // Добавляем количество найденных подмассивов
        prefixSumCount[currentSum] = prefixSumCount.getOrDefault(currentSum, 0) + 1 // Обновляем счетчик суммы префикса
    }

    writer.write(count.toString()) // Выводим результат
    writer.newLine()

    reader.close()
    writer.close()
}