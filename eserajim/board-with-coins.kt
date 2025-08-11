import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.ArrayDeque

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val parts = reader.readLine().split(" ")
    val n = parts[0].toInt()
    val m = parts[1].toInt()
    val totalCells = n * m

    var initState = 0
    for (i in 0 until n) {
        val line = reader.readLine().trim()
        for (j in 0 until m) {
            if (line[j] == '1') {
                initState = initState or (1 shl (i * m + j))
            }
        }
    }

    var targetA = 0
    var targetB = 0
    for (i in 0 until n) {
        for (j in 0 until m) {
            val pos = i * m + j
            if ((i + j) % 2 == 1) {
                targetA = targetA or (1 shl pos)
            } else {
                targetB = targetB or (1 shl pos)
            }
        }
    }

    if (initState == targetA || initState == targetB) {
        writer.write("0")
        writer.newLine()
        writer.flush()
        reader.close()
        writer.close()
        return
    }

    val moves = mutableListOf<Int>()
    for (i in 0 until n) {
        for (j in 0 until m) {
            val pos = i * m + j
            if (j + 1 < m) {
                val posRight = i * m + (j + 1)
                moves.add((1 shl pos) or (1 shl posRight))
            }
            if (i + 1 < n) {
                val posDown = (i + 1) * m + j
                moves.add((1 shl pos) or (1 shl posDown))
            }
        }
    }

    val size = 1 shl totalCells
    val visited = BooleanArray(size)
    val deque = ArrayDeque<Pair<Int, Int>>()
    visited[initState] = true
    deque.add(Pair(initState, 0))

    var ans = -1
    while (deque.isNotEmpty()) {
        val (cur, dist) = deque.removeFirst()
        if (cur == targetA || cur == targetB) {
            ans = dist
            break
        }
        for (move in moves) {
            val next = cur xor move
            if (!visited[next]) {
                visited[next] = true
                deque.add(Pair(next, dist + 1))
            }
        }
    }

    writer.write(ans.toString())
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
