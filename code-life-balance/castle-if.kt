import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val a = reader.readLine().toInt()
    val b = reader.readLine().toInt()
    val c = reader.readLine().toInt()
    val d = reader.readLine().toInt()
    val e = reader.readLine().toInt()

    val brickSides = listOf(a, b, c).sorted()
    val holeSides = listOf(d, e).sorted()
    val canPass = brickSides[0] <= holeSides[0] && brickSides[1] <= holeSides[1]
    writer.write(if (canPass) "YES" else "NO")
    writer.newLine()

    reader.close()
    writer.close()
}
