import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    // Считываем количество пар синонимов
    val n = reader.readLine().toInt()

    // Создаём словарь, в котором для каждого слова
    // будет храниться его синоним
    val dictionary = HashMap<String, String>()

    // Считываем пары слов и сохраняем их в словарь в обоих направлениях:
    // s1 -> s2 и s2 -> s1
    repeat(n) {
        val (s1, s2) = reader.readLine().split(" ")
        dictionary[s1] = s2
        dictionary[s2] = s1
    }

    // Считываем искомое слово
    val query = reader.readLine()

    // Выводим синоним к считанному слову
    writer.write(dictionary[query] ?: "")
    writer.newLine()

    reader.close()
    writer.close()
}