import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Читаем входную строку
    val text = reader.readLine()
    
    // Карта для подсчёта частоты пар
    val pairCount = mutableMapOf<String, Int>()
    
    // Разбиваем текст на слова
    val words = text.split("\\s+".toRegex())
    
    // Считаем все пары в каждом слове
    for (word in words) {
        if (word.length < 2) continue
        for (i in 0 until word.length - 1) {
            val pair = word.substring(i, i + 2)
            pairCount[pair] = pairCount.getOrDefault(pair, 0) + 1
        }
    }
    
    // Находим пару с максимальной частотой
    var maxCount = 0
    var result = ""
    
    for ((pair, count) in pairCount) {
        if (count > maxCount || (count == maxCount && pair > result)) {
            maxCount = count
            result = pair
        }
    }
    
    writer.write(result)
    writer.newLine()

    reader.close()
    writer.close()
}
