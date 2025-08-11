import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val studentLanguages = Array(n) { mutableSetOf<String>() }

    for (i in 0 until n) {
        val m = reader.readLine().toInt()
        repeat(m) {
            val lang = reader.readLine()
            studentLanguages[i].add(lang)
        }
    }

    val allKnow = if (n == 1) {
        studentLanguages[0]
    } else {
        var result = studentLanguages[0].toMutableSet()
        for (i in 1 until n) {
            result.retainAll(studentLanguages[i])
        }
        result
    }

    val anyKnow = mutableSetOf<String>()
    for (i in 0 until n) {
        anyKnow.addAll(studentLanguages[i])
    }

    writer.write("${allKnow.size}\n")
    for (lang in allKnow.sorted()) {
        writer.write("$lang\n")
    }

    writer.write("${anyKnow.size}\n")
    for (lang in anyKnow.sorted()) {
        writer.write("$lang\n")
    }

    reader.close()
    writer.close()
}