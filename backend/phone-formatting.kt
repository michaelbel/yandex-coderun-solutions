import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun normalizePhone(phone: String): Pair<String, String> {
    val cleanPhone = phone.replace("-", "").replace("(", "").replace(")", "")
    var code = "495"
    var number = ""
    when {
        cleanPhone.startsWith("+7") -> {
            code = cleanPhone.substring(2, 5)
            number = cleanPhone.substring(5)
        }
        cleanPhone.startsWith("8") -> {
            code = cleanPhone.substring(1, 4)
            number = cleanPhone.substring(4)
        }
        cleanPhone.length == 7 -> {
            number = cleanPhone
        }
    }
    return Pair(code, number)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val newPhone = reader.readLine()
    val (newCode, newNumber) = normalizePhone(newPhone)
    repeat(3) {
        val existingPhone = reader.readLine()
        val (existingCode, existingNumber) = normalizePhone(existingPhone)
        if (newCode == existingCode && newNumber == existingNumber) {
            writer.write("YES")
        } else {
            writer.write("NO")
        }
        writer.newLine()
    }
    reader.close()
    writer.close()
}
