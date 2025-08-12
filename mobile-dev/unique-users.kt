import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun normalizeEmail(email: String): String {
    val (login, domain) = email.split("@")
    val normalizedLogin = login.takeWhile { it != '-' }.replace(".", "")
    val domainParts = domain.split(".")
    val baseDomain = domainParts.dropLast(1).joinToString(".")
    return "$normalizedLogin@$baseDomain"
}

fun countUniqueUsers(n: Int, emails: List<String>): Int {
    val uniqueUsers = mutableSetOf<String>()
    for (email in emails) {
        uniqueUsers.add(normalizeEmail(email))
    }
    return uniqueUsers.size
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val emails = List(n) { reader.readLine() }
    val uniqueCount = countUniqueUsers(n, emails)
    writer.write(uniqueCount.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
