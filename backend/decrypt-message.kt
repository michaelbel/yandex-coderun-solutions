import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.StringBuilder
import java.util.HashMap

fun applyShift(word: String, shift: Int): String {
    val sb = StringBuilder(word.length)
    for (char in word) {
        val shifted = (char.code - 'a'.code + shift) % 26
        sb.append('a' + shifted)
    }
    return sb.toString()
}

fun getCanonicalForm(word: String): String {
    if (word.isEmpty()) return ""
    val shift = (26 - (word[0].code - 'a'.code)) % 26
    return applyShift(word, shift)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val bookWords = reader.readLine().split(' ').filter { it.isNotEmpty() }.toSet()
    val canonicalMap = HashMap<String, String>()
    for (word in bookWords) {
        val canonical = getCanonicalForm(word)
        canonicalMap[canonical] = word
    }
    val n = reader.readLine().toInt()
    for (i in 0 until n) {
        val encrypted = reader.readLine()
        if (encrypted.isNotEmpty()) {
            val canonicalEncrypted = getCanonicalForm(encrypted)
            val original = canonicalMap[canonicalEncrypted] ?: ""
            writer.write(original)
            writer.newLine()
        } else {
            writer.newLine()
        }
    }
    writer.flush()
    reader.close()
    writer.close()
}
