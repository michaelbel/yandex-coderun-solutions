import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val s = reader.readLine()
    for (k in 1..n) {
        var possible = true
        val schema = CharArray(k) { '?' }
        for (i in 0 until n) {
            val currentSymbol = s[i]
            if (currentSymbol == '#') continue
            val schemaIndex = i % k
            val existingSchemaSymbol = schema[schemaIndex]
            if (existingSchemaSymbol == '?') {
                schema[schemaIndex] = currentSymbol
            } else if (existingSchemaSymbol != currentSymbol) {
                possible = false
                break
            }
        }
        if (possible) {
            writer.write(k.toString())
            writer.flush()
            return
        }
    }
    reader.close()
    writer.close()
}
