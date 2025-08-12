import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`), 1 shl 16)
    val writer = BufferedWriter(OutputStreamWriter(System.out), 1 shl 16)
    fun readInt(): Int {
        var c = reader.read()
        while (c <= ' '.code) c = reader.read()
        var neg = false
        if (c == '-'.code) {
            neg = true
            c = reader.read()
        }
        var x = 0
        while (c >= '0'.code) {
            x = x * 10 + (c - '0'.code)
            c = reader.read()
        }
        return if (neg) -x else x
    }
    val mrow = readInt()
    val mcol = readInt()
    val ascii = Array(mrow) { reader.readLine().toCharArray() }
    val row = readInt()
    val hi = IntArray(row) { readInt() }
    val col = readInt()
    val wi = IntArray(col) { readInt() }
    val rowSep = IntArray(row + 1)
    for (i in 1..row) rowSep[i] = rowSep[i - 1] + hi[i - 1] + 1
    val colSep = IntArray(col + 1)
    for (j in 1..col) colSep[j] = colSep[j - 1] + wi[j - 1] + 1
    val borderUD = Array(row + 1) { BooleanArray(col + 1) }
    val borderLR = Array(row + 2) { BooleanArray(col + 1) }
    for (i in 0..row) {
        for (j in 1..col) {
            borderUD[i][j] = if (i == 0 || i == row) true else {
                val p = rowSep[i]
                var sep = false
                for (q in (colSep[j - 1] + 1) until colSep[j]) {
                    val ch = ascii[p][q]
                    if (ch == '-' || ch == '+') {
                        sep = true
                        break
                    }
                }
                sep
            }
        }
    }
    for (i in 1..row) {
        for (j in 0..col) {
            borderLR[i][j] = if (j == 0 || j == col) true else {
                val q = colSep[j]
                var sep = false
                for (p in (rowSep[i - 1] + 1) until rowSep[i]) {
                    val ch = ascii[p][q]
                    if (ch == '|' || ch == '+') {
                        sep = true
                        break
                    }
                }
                sep
            }
        }
    }
    val visited = Array(row + 2) { IntArray(col + 2) }
    var stamp = 0
    val maxCells = (row + 1) * (col + 1)
    val qx = IntArray(maxCells)
    val qy = IntArray(maxCells)
    data class Component(val minR: Int, val maxR: Int, val minC: Int, val maxC: Int, val size: Int)
    fun bfs(sr: Int, sc: Int): Component {
        stamp++
        var head = 0
        var tail = 0
        qx[tail] = sr
        qy[tail] = sc
        visited[sr][sc] = stamp
        tail++
        var minR = sr
        var maxR = sr
        var minC = sc
        var maxC = sc
        var cnt = 1
        while (head < tail) {
            val r = qx[head]
            val c = qy[head]
            head++
            if (r > 1 && !borderUD[r - 1][c] && visited[r - 1][c] != stamp) {
                visited[r - 1][c] = stamp
                qx[tail] = r - 1
                qy[tail] = c
                tail++
                cnt++
                if (r - 1 < minR) minR = r - 1
                if (r - 1 > maxR) maxR = r - 1
            }
            if (r < row && !borderUD[r][c] && visited[r + 1][c] != stamp) {
                visited[r + 1][c] = stamp
                qx[tail] = r + 1
                qy[tail] = c
                tail++
                cnt++
                if (r + 1 < minR) minR = r + 1
                if (r + 1 > maxR) maxR = r + 1
            }
            if (c > 1 && !borderLR[r][c - 1] && visited[r][c - 1] != stamp) {
                visited[r][c - 1] = stamp
                qx[tail] = r
                qy[tail] = c - 1
                tail++
                cnt++
                if (c - 1 < minC) minC = c - 1
                if (c - 1 > maxC) maxC = c - 1
            }
            if (c < col && !borderLR[r][c] && visited[r][c + 1] != stamp) {
                visited[r][c + 1] = stamp
                qx[tail] = r
                qy[tail] = c + 1
                tail++
                cnt++
                if (c + 1 < minC) minC = c + 1
                if (c + 1 > maxC) maxC = c + 1
            }
        }
        return Component(minR, maxR, minC, maxC, cnt)
    }
    fun cross(i: Int, j: Int): Char {
        val hasVert = (i > 0 && borderLR[i][j]) || (i < row && borderLR[i + 1][j])
        val hasHorz = (j > 0 && borderUD[i][j]) || (j < col && borderUD[i][j + 1])
        return when {
            hasVert && hasHorz -> '+'
            hasVert -> '|'
            hasHorz -> '-'
            else -> ' '
        }
    }
    val sb = StringBuilder()
    fun printTable() {
        for (i in 0..row) {
            sb.setLength(0)
            for (j in 0..col) {
                sb.append(cross(i, j))
                if (j < col) repeat(wi[j]) { sb.append(if (borderUD[i][j + 1]) '-' else ' ') }
            }
            writer.append(sb).append('\n')
            if (i < row) {
                repeat(hi[i]) {
                    sb.setLength(0)
                    for (j in 0..col) {
                        sb.append(if (borderLR[i + 1][j]) '|' else ' ')
                        if (j < col) repeat(wi[j]) { sb.append(' ') }
                    }
                    writer.append(sb).append('\n')
                }
            }
        }
    }
    fun parsePos(pos: String): Pair<Int, Int> {
        var cAcc = 0
        var idx = 0
        while (idx < pos.length && pos[idx] in 'A'..'Z') {
            cAcc = cAcc * 26 + (pos[idx] - 'A' + 1)
            idx++
        }
        var rAcc = 0
        while (idx < pos.length) {
            rAcc = rAcc * 10 + (pos[idx] - '0')
            idx++
        }
        return Pair(rAcc, cAcc)
    }
    val q = readInt()
    for (it in 1..q) {
        val parts = reader.readLine().split(' ')
        when (parts[0]) {
            "split" -> {
                val (r, c) = parsePos(parts[1])
                val comp = bfs(r, c)
                if (comp.size <= 1) {
                    writer.append("Can not split elementary cell\n")
                } else {
                    for (rr in comp.minR..comp.maxR) {
                        for (cc in comp.minC until comp.maxC) borderLR[rr][cc] = true
                    }
                    for (rr in comp.minR until comp.maxR) {
                        for (cc in comp.minC..comp.maxC) borderUD[rr][cc] = true
                    }
                    writer.append("Split onto ${comp.size} cells\n")
                    printTable()
                }
            }
            "merge" -> {
                val (r1, c1) = parsePos(parts[1])
                val comp1 = bfs(r1, c1)
                val (r2, c2) = parsePos(parts[2])
                if (visited[r2][c2] == stamp) {
                    writer.append("Can not merge cell with itself\n")
                } else {
                    val comp2 = bfs(r2, c2)
                    if (comp1.minR == comp2.minR && comp1.maxR == comp2.maxR && (comp1.maxC + 1 == comp2.minC || comp2.maxC + 1 == comp1.minC)) {
                        val bc = if (comp1.maxC + 1 == comp2.minC) comp1.maxC else comp2.maxC
                        for (rr in comp1.minR..comp1.maxR) borderLR[rr][bc] = false
                        writer.append("Merged horizontally-aligned cells\n")
                        printTable()
                    } else if (comp1.minC == comp2.minC && comp1.maxC == comp2.maxC && (comp1.maxR + 1 == comp2.minR || comp2.maxR + 1 == comp1.minR)) {
                        val br = if (comp1.maxR + 1 == comp2.minR) comp1.maxR else comp2.maxR
                        for (cc in comp1.minC..comp1.maxC) borderUD[br][cc] = false
                        writer.append("Merged vertically-aligned cells\n")
                        printTable()
                    } else {
                        writer.append("Can not merge unaligned cells\n")
                    }
                }
            }
        }
    }
    writer.flush()
}
