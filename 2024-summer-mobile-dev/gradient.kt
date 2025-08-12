import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val (w, h) = reader.readLine().split(" ").map { it.toInt() }
    val rowSums = LongArray(h) { 0L }
    val colSums = LongArray(w) { 0L }
    for (row in 0 until h) {
        for (col in 0 until w) {
            val count = (row + 1).toLong() * (h - row).toLong() * (col + 1).toLong() * (w - col).toLong()
            rowSums[row] += count
            colSums[col] += count
        }
    }
    writer.write(rowSums.joinToString(" "))
    writer.newLine()
    writer.write(colSums.joinToString(" "))
    reader.close()
    writer.close()
}
