import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.math.BigInteger
import java.util.StringTokenizer

fun main(args: Array<String>) {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))
    val N = reader.readLine().toInt()
    val grid = Array(N) { IntArray(N) }
    for (i in 0 until N) {
        val st = StringTokenizer(reader.readLine())
        for (j in 0 until N) {
            grid[i][j] = st.nextToken().toInt()
        }
    }
    val dpLen = Array(N) { IntArray(N) }
    val dpCnt = Array(N) { Array<BigInteger>(N) { BigInteger.ZERO } }
    for (i in 0 until N) {
        for (j in 0 until N) {
            if (i == 0 || i == N - 1 || j == 0 || j == N - 1) {
                dpLen[i][j] = 1
                dpCnt[i][j] = BigInteger.ONE
            }
        }
    }
    data class Cell(val value: Int, val i: Int, val j: Int)
    val cells = ArrayList<Cell>(N * N)
    for (i in 0 until N) {
        for (j in 0 until N) {
            cells.add(Cell(grid[i][j], i, j))
        }
    }
    cells.sortByDescending { it.value }
    val dx = intArrayOf(1, -1, 0, 0)
    val dy = intArrayOf(0, 0, 1, -1)
    for (cell in cells) {
        val i = cell.i
        val j = cell.j
        val v = cell.value
        for (d in 0..3) {
            val ni = i + dx[d]
            val nj = j + dy[d]
            if (ni in 0 until N && nj in 0 until N && grid[ni][nj] > v) {
                val neighborLen = dpLen[ni][nj]
                if (neighborLen > 0) {
                    val cand = neighborLen + 1
                    when {
                        cand > dpLen[i][j] -> {
                            dpLen[i][j] = cand
                            dpCnt[i][j] = dpCnt[ni][nj]
                        }
                        cand == dpLen[i][j] -> {
                            dpCnt[i][j] = dpCnt[i][j].add(dpCnt[ni][nj])
                        }
                    }
                }
            }
        }
    }
    var maxLen = 0
    for (i in 0 until N) {
        for (j in 0 until N) {
            if (dpLen[i][j] > maxLen) {
                maxLen = dpLen[i][j]
            }
        }
    }
    var totalCnt = BigInteger.ZERO
    for (i in 0 until N) {
        for (j in 0 until N) {
            if (dpLen[i][j] == maxLen) {
                totalCnt = totalCnt.add(dpCnt[i][j])
            }
        }
    }
    writer.write("$maxLen $totalCnt")
    writer.newLine()
    writer.flush()
    reader.close()
    writer.close()
}
