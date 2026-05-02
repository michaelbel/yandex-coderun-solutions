import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val n = reader.readLine().toInt()
    val field = Array(7) { Array(7) { 3 } }
    val scores = arrayOf(30, 30)
    val my = MutableInt(3)
    val mx = MutableInt(3)
    val md = MutableInt(2)

    for (step in 0 until n) {
        val data = reader.readLine().split(" ")
        val rotate = data[0]
        val dice = data[1].toInt()
        val cell1 = data[2]
        val cell2 = data[3]
        val player = step % 2
        val opp = (step + 1) % 2

        when (rotate) {
            "RIGHT" -> md.value = (md.value + 1).remEuclid(4)
            "LEFT" -> md.value = (md.value - 1).remEuclid(4)
        }

        when (md.value) {
            0 -> throwDice(my, mx, md, dice)
            1 -> throwDice(mx, my, md, -dice)
            2 -> throwDice(my, mx, md, -dice)
            3 -> throwDice(mx, my, md, dice)
        }

        val cy = my.value
        val cx = mx.value
        if (field[cy][cx] == opp) {
            val pay = payment(field, cy, cx, opp)
            scores[player] -= pay
            scores[opp] += pay
        }

        var yc = cy
        var xc = cx
        listOf(cell1, cell2).forEach { d ->
            when (d) {
                "E" -> xc += 1
                "W" -> xc -= 1
                "N" -> yc -= 1
                "S" -> yc += 1
            }
            if (field[yc][xc] != player) {
                if (field[yc][xc] == opp) {
                    scores[opp] -= 1
                }
                scores[player] += 1
                field[yc][xc] = player
            }
        }
    }

    writer.write("${scores[0]} ${scores[1]}")
    writer.newLine()

    reader.close()
    writer.close()
}

fun Int.remEuclid(m: Int): Int = ((this % m) + m) % m

class MutableInt(var value: Int)

fun throwDice(a: MutableInt, b: MutableInt, d: MutableInt, s: Int) {
    a.value += s
    if (a.value > 6 && b.value == 0) {
        b.value = a.value - 7
        a.value = 6
        d.value = 3 - d.value
    } else if (a.value > 6) {
        val inc = if (b.value % 2 == 1) 1 else -1
        b.value += inc
        a.value = 13 - a.value
        d.value = (d.value + 2).remEuclid(4)
    } else if (a.value < 0 && b.value == 6) {
        b.value = 7 + a.value
        a.value = 0
        d.value = 3 - d.value
    } else if (a.value < 0) {
        val inc = if (b.value % 2 == 1) -1 else 1
        b.value += inc
        a.value = -1 * (a.value + 1)
        d.value = (d.value + 2).remEuclid(4)
    }
}

fun payment(field: Array<Array<Int>>, sy: Int, sx: Int, target: Int): Int {
    val visited = HashSet<Pair<Int, Int>>()
    val stack = ArrayDeque<Pair<Int, Int>>()
    stack.add(sy to sx)
    while (stack.isNotEmpty()) {
        val (i, j) = stack.removeLast()
        if (!visited.add(i to j)) continue
        for ((di, dj) in listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)) {
            val ni = i + di
            val nj = j + dj
            if (ni >= 0 && ni < 7 && nj >= 0 && nj < 7 && field[ni][nj] == target && !visited.contains(ni to nj)) {
                stack.add(ni to nj)
            }
        }
    }
    return visited.size
}
