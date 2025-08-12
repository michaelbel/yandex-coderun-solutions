import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class Person(var a: Int, var row: Int, var pos: Int, var seated: Boolean = false)

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val persons = ArrayList<Person>(n)
    val occupiedSeatsInRow = mutableMapOf<Int, MutableSet<Char>>()
    fun countNeighborsToStandUp(row: Int, seat: Char): Int {
        val occupied = occupiedSeatsInRow.getOrPut(row) { mutableSetOf() }
        return when (seat) {
            'A' -> { var cnt = 0; if ('B' in occupied) cnt++; if ('C' in occupied) cnt++; cnt }
            'B' -> if ('C' in occupied) 1 else 0
            'C' -> 0
            'D' -> 0
            'E' -> if ('D' in occupied) 1 else 0
            'F' -> { var cnt = 0; if ('D' in occupied) cnt++; if ('E' in occupied) cnt++; cnt }
            else -> 0
        }
    }
    repeat(n) { i ->
        val line = reader.readLine().trim().split(" ")
        val aInput = line[0].toInt()
        val seatString = line[1]
        val rowPart = seatString.substring(0, seatString.length - 1)
        val seatPart = seatString.last()
        val row = rowPart.toInt()
        val k = countNeighborsToStandUp(row, seatPart)
        val aTime = when (k) {
            1 -> aInput + 5
            2 -> aInput + 15
            else -> aInput
        }
        occupiedSeatsInRow[row]!!.add(seatPart)
        persons.add(Person(aTime, row + 180, 180 - i))
    }
    persons.reverse()
    var time = 0
    var currentList = persons
    while (currentList.isNotEmpty()) {
        time++
        val newList = ArrayList<Person>(currentList.size)
        val size = currentList.size
        for (i in size - 1 downTo 0) {
            val p = currentList[i]
            if (p.pos == p.row) {
                if (p.a == 0) {
                    p.seated = true
                } else {
                    p.a--
                    newList.add(p)
                }
            } else {
                val canMoveForward = if (i == size - 1) true else {
                    val nextP = currentList[i + 1]
                    (p.pos + 1 != nextP.pos) || nextP.seated
                }
                if (canMoveForward) {
                    p.pos++
                }
                newList.add(p)
            }
        }
        newList.reverse()
        val filtered = newList.filter { !it.seated }
        currentList = ArrayList(filtered)
    }
    writer.write("${time - 1}\n")
    writer.flush()
    reader.close()
    writer.close()
}
