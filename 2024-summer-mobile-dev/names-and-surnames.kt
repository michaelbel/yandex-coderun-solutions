import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val a = reader.readLine().split(" ").map { it.toInt() }
    val indexed = a.withIndex().sortedBy { it.value }
    var playerLevel = 1
    var penaltyPoints = 0
    for ((index, rivalLevel) in indexed) {
        if (playerLevel < rivalLevel) {
            writer.write("Impossible")
            reader.close()
            writer.close()
            return
        }
        playerLevel++
        if (playerLevel <= rivalLevel * 2) {
            penaltyPoints++
            if (penaltyPoints == 3) {
                playerLevel--
                penaltyPoints = 0
            }
        }
    }
    writer.write("Possible")
    writer.newLine()
    writer.write(indexed.joinToString(" ") { (it.index + 1).toString() })
    reader.close()
    writer.close()
}
