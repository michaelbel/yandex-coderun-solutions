import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// Функция для нахождения максимальной длины подстроки, состоящей из одного символа, с возможностью заменить до k символов
fun maxBeauty(s: String, k: Int): Int {
    // Вспомогательная функция для поиска максимальной длины подстроки из символа c
    fun maxConsecutive(c: Char): Int {
        var left = 0 // Левый указатель окна
        var right = 0 // Правый указатель окна
        var maxLen = 0 // Максимальная длина подстроки
        var changes = 0 // Количество замененных символов

        while (right < s.length) {
            if (s[right] != c) changes++ // Увеличиваем счетчик замен, если текущий символ не равен c
            
            // Если число замен превысило k, сдвигаем левый указатель, пока снова не станем допустимыми
            while (changes > k) {
                if (s[left] != c) changes--
                left++
            }
            
            maxLen = maxOf(maxLen, right - left + 1) // Обновляем максимальную длину
            right++ // Двигаем правый указатель
        }
        return maxLen
    }

    // Проверяем максимальную длину подстроки для всех символов от 'a' до 'z'
    return ('a'..'z').maxOf { maxConsecutive(it) }
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val k = reader.readLine().toInt() // Читаем число допустимых замен
    val s = reader.readLine() // Читаем строку

    writer.write("${maxBeauty(s, k)}\n") // Выводим результат

    reader.close()
    writer.flush()
    writer.close()
}
