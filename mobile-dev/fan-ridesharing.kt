import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun canAccommodate(teams: IntArray, rooms: MutableMap<Int, Int>): Boolean {
    val teamFreq = IntArray(10001)
    for (team in teams) {
        teamFreq[team]++
    }
    val roomFreq = IntArray(10001)
    for ((capacity, count) in rooms) {
        roomFreq[capacity] += count
    }
    for (size in 10000 downTo 1) {
        if (teamFreq[size] == 0) continue
        var teamsLeft = teamFreq[size]
        for (roomSize in size..10000) {
            if (teamsLeft == 0) break
            if (roomFreq[roomSize] > 0) {
                val used = minOf(teamsLeft, roomFreq[roomSize])
                teamsLeft -= used
                roomFreq[roomSize] -= used
            }
        }
        if (teamsLeft > 0) return false
    }
    return true
}

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val n = reader.readLine().toInt()
    val teams = reader.readLine().split(" ").map { it.toInt() }.toIntArray()
    val k = reader.readLine().toInt()
    val rooms = mutableMapOf<Int, Int>()
    for (i in 1..k) {
        val (capacity, count) = reader.readLine().split(" ").map { it.toInt() }
        rooms[capacity] = rooms.getOrDefault(capacity, 0) + count
    }
    if (canAccommodate(teams, rooms)) {
        writer.write("Yes")
    } else {
        writer.write("No")
    }
    writer.flush()
    reader.close()
    writer.close()
}
