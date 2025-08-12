import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class FruitBox(val m: Long, val o: Long)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val totalBoxes = 2 * n - 1

    val boxes = ArrayList<FruitBox>(totalBoxes)
    var totalMandarins = 0L
    var totalOranges = 0L

    for (i in 0 until totalBoxes) {
        val parts = reader.readLine().split(" ")
        val mandarins = parts[0].toLong()
        val oranges = parts[1].toLong()
        boxes.add(FruitBox(mandarins, oranges))
        totalMandarins += mandarins
        totalOranges += oranges
    }

    val requiredMandarins = (totalMandarins + 1) / 2
    val requiredOranges = (totalOranges + 1) / 2

    boxes.sortByDescending { it.o }

    var currentMandarins = 0L
    var currentOranges = 0L

    currentMandarins += boxes[0].m
    currentOranges += boxes[0].o

    for (i in 1 until n) {
        val index1 = 2 * i - 1
        val index2 = 2 * i
        if (boxes[index1].m >= boxes[index2].m) {
            currentMandarins += boxes[index1].m
            currentOranges += boxes[index1].o
        } else {
            currentMandarins += boxes[index2].m
            currentOranges += boxes[index2].o
        }
    }

    if (currentMandarins >= requiredMandarins && currentOranges >= requiredOranges) {
        writer.write("Yes")
    } else {
        writer.write("No")
    }
    writer.newLine()
    reader.close()
    writer.close()
}
