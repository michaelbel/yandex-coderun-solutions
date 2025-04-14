import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()

    // Множество языков каждого школьника
    val studentLanguages = Array(n) { mutableSetOf<String>() }

    // Заполняем языки для каждого школьника
    for (i in 0 until n) {
        val m = reader.readLine().toInt()
        repeat(m) {
            val lang = reader.readLine()
            studentLanguages[i].add(lang)
        }
    }

    // Языки, которые знают все (пересечение)
    val allKnow = if (n == 1) {
        studentLanguages[0]
    } else {
        var result = studentLanguages[0].toMutableSet()
        for (i in 1 until n) {
            result.retainAll(studentLanguages[i])
        }
        result
    }

    // Языки, которые знает хотя бы один (объединение)
    val anyKnow = mutableSetOf<String>()
    for (i in 0 until n) {
        anyKnow.addAll(studentLanguages[i])
    }

    // Выводим языки, которые знают все
    writer.write("${allKnow.size}\n")
    for (lang in allKnow.sorted()) {
        writer.write("$lang\n")
    }

    // Выводим языки, которые знает хотя бы один
    writer.write("${anyKnow.size}\n")
    for (lang in anyKnow.sorted()) {
        writer.write("$lang\n")
    }

    reader.close()
    writer.close()
}