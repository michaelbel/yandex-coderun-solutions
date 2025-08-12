import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val route = mutableListOf<Pair<String, Int>>()
    while (true) {
        val line = reader.readLine() ?: break
        val (direction, distance) = line.split(" ")
        route.add(Pair(direction, distance.toInt()))
    }
    fun isOpposite(dir1: String, dir2: String): Boolean {
        return (dir1 == "TOP" && dir2 == "BOTTOM") ||
               (dir1 == "BOTTOM" && dir2 == "TOP") ||
               (dir1 == "LEFT" && dir2 == "RIGHT") ||
               (dir1 == "RIGHT" && dir2 == "LEFT")
    }
    var i = 0
    while (i < route.size - 1) {
        val (currDir, currDist) = route[i]
        val (nextDir, nextDist) = route[i + 1]
        if (isOpposite(currDir, nextDir)) {
            if (currDist == nextDist) {
                route.removeAt(i + 1)
                route.removeAt(i)
                if (i > 0) i--
            } else if (currDist > nextDist) {
                route[i] = Pair(currDir, currDist - nextDist)
                route.removeAt(i + 1)
            } else {
                route[i + 1] = Pair(nextDir, nextDist - currDist)
                route.removeAt(i)
            }
        } else if (currDir == nextDir) {
            route[i] = Pair(currDir, currDist + nextDist)
            route.removeAt(i + 1)
        } else {
            i++
        }
    }
    for ((direction, distance) in route) {
        writer.write("$direction $distance")
        writer.newLine()
    }
    reader.close()
    writer.close()
}
