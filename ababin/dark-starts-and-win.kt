import java.io.BufferedInputStream

private class FastScanner {
    private val input = BufferedInputStream(System.`in`)
    private val buffer = ByteArray(1 shl 16)
    private var len = 0
    private var ptr = 0

    private fun readByte(): Int {
        if (ptr >= len) {
            len = input.read(buffer)
            ptr = 0
            if (len <= 0) return -1
        }
        return buffer[ptr++].toInt()
    }

    private fun isSpace(c: Int): Boolean {
        return c == 10 || c == 13 || c == 32 || c == 9
    }

    private fun skipSpaces() {
        while (true) {
            val c = readByte()
            if (c == -1) return
            if (!isSpace(c)) {
                ptr--
                return
            }
        }
    }

    fun readInt(): Int {
        skipSpaces()
        var c = readByte()
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = readByte()
        }
        var num = 0
        while (c in '0'.code..'9'.code) {
            num = num * 10 + (c - '0'.code)
            c = readByte()
        }
        return num * sign
    }

    fun readRow(): CharArray {
        skipSpaces()
        val chars = ArrayList<Char>(16)
        while (true) {
            val c = readByte()
            if (c == -1 || isSpace(c)) break
            chars.add(c.toChar())
        }
        return CharArray(chars.size) { chars[it] }
    }
}

private class FastOutput(capacity: Int) {
    private val out = ByteArray(capacity.coerceAtLeast(64))
    private var size = 0

    private fun ensure(extra: Int) {
        if (size + extra <= out.size) return
    }

    private val buffer = StringBuilder()

    private fun writeIntNoNl(x: Int) {
        buffer.append(x)
    }

    fun writeLineInt(x: Int) {
        writeIntNoNl(x)
        buffer.append('\n')
    }

    fun writePair(a: Int, b: Int) {
        writeIntNoNl(a)
        buffer.append(' ')
        writeIntNoNl(b)
        buffer.append('\n')
    }

    fun flush() {
        print(buffer.toString())
    }
}

private fun inBounds(x: Int, n: Int): Boolean {
    return x >= 0 && x < n
}

fun main() {
    val fs = FastScanner()
    val n = fs.readInt()
    val nn = n * n

    val whites = ArrayList<Int>(nn / 4)
    val blacks = ArrayList<Int>(nn / 4)
    val isBlack = ByteArray(nn)

    repeat(n) { r ->
        val row = fs.readRow()
        for (c in 0 until n) {
            when (row[c]) {
                'W' -> whites.add(r * n + c)
                'B' -> {
                    val id = r * n + c
                    blacks.add(id)
                    isBlack[id] = 1
                }
            }
        }
    }

    val out = FastOutput((whites.size + 2) * 16)

    if (whites.isEmpty()) {
        if (blacks.isNotEmpty()) {
            val cell = blacks[0]
            out.writeLineInt(1)
            out.writePair(cell / n + 1, cell % n + 1)
        } else {
            out.writeLineInt(-1)
        }
        out.flush()
        return
    }

    fun cellType(cell: Int): Int {
        val r = cell / n
        return r and 1
    }

    val firstWhiteType = cellType(whites[0])
    for (w in whites) {
        if (cellType(w) != firstWhiteType) {
            out.writeLineInt(-1)
            out.flush()
            return
        }
    }

    fun compParity(r: Int, c: Int): Int {
        return ((r shr 1) + (c shr 1)) and 1
    }

    fun tryBuild(moverType: Int, parityP: Int): IntArray? {
        if (firstWhiteType != 1 - moverType) return null

        val idOfCell = IntArray(nn) { -1 }
        val vertexCells = ArrayList<Int>(whites.size * 2 + 4)
        val deg = ArrayList<Int>(whites.size * 2 + 4)

        fun getId(cell: Int): Int {
            val cur = idOfCell[cell]
            if (cur >= 0) return cur
            val nid = vertexCells.size
            idOfCell[cell] = nid
            vertexCells.add(cell)
            deg.add(0)
            return nid
        }

        val eCount = whites.size
        val u = IntArray(eCount)
        val v = IntArray(eCount)

        for (i in 0 until eCount) {
            val w = whites[i]
            val r = w / n
            val c = w % n

            var uCell = -1
            var vCell = -1

            val r1 = r - 1
            val c1 = c - 1
            val r2 = r + 1
            val c2 = c + 1
            if (inBounds(r1, n) && inBounds(c1, n) && inBounds(r2, n) && inBounds(c2, n)) {
                if ((r1 and 1) == moverType && compParity(r1, c1) == parityP) {
                    uCell = r1 * n + c1
                    vCell = r2 * n + c2
                }
            }

            if (uCell == -1) {
                val r3 = r - 1
                val c3 = c + 1
                val r4 = r + 1
                val c4 = c - 1
                if (inBounds(r3, n) && inBounds(c3, n) && inBounds(r4, n) && inBounds(c4, n)) {
                    if ((r3 and 1) == moverType && compParity(r3, c3) == parityP) {
                        uCell = r3 * n + c3
                        vCell = r4 * n + c4
                    }
                }
            }

            if (uCell == -1) return null

            val uid = getId(uCell)
            val vid = getId(vCell)
            u[i] = uid
            v[i] = vid
            deg[uid] = deg[uid] + 1
            deg[vid] = deg[vid] + 1
        }

        var start = -1
        for (i in vertexCells.indices) {
            if (deg[i] == 0) continue
            val cell = vertexCells[i]
            if (isBlack[cell].toInt() == 1) {
                if (start == -1) start = i else return null
            }
        }
        if (start == -1) return null

        var odd = 0
        for (i in deg.indices) {
            if (deg[i] > 0 && (deg[i] and 1) == 1) odd++
        }
        if (odd != 0 && odd != 2) return null
        if (odd == 2 && (deg[start] and 1) == 0) return null

        val vCnt = vertexCells.size
        val offset = IntArray(vCnt + 1)
        for (i in 0 until vCnt) {
            offset[i + 1] = offset[i] + deg[i]
        }

        val adj = IntArray(offset[vCnt])
        val ptr = offset.clone()

        for (e in 0 until eCount) {
            val a = u[e]
            val b = v[e]
            adj[ptr[a]++] = e
            adj[ptr[b]++] = e
        }

        val it = offset.clone()
        val used = ByteArray(eCount)
        var usedCnt = 0

        val stack = IntArray(eCount + 1)
        val res = IntArray(eCount + 1)
        var stackSize = 0
        var resSize = 0

        stack[stackSize++] = start

        while (stackSize > 0) {
            val cur = stack[stackSize - 1]
            var p = it[cur]
            val end = offset[cur + 1]
            while (p < end && used[adj[p]].toInt() != 0) p++
            it[cur] = p
            if (p == end) {
                res[resSize++] = cur
                stackSize--
            } else {
                val e = adj[p]
                it[cur] = p + 1
                if (used[e].toInt() != 0) continue
                used[e] = 1
                usedCnt++
                val nxt = if (u[e] == cur) v[e] else u[e]
                stack[stackSize++] = nxt
            }
        }

        if (usedCnt != eCount || resSize != eCount + 1) return null

        val pathCells = IntArray(resSize)
        var j = 0
        for (i in resSize - 1 downTo 0) {
            pathCells[j++] = vertexCells[res[i]]
        }
        return pathCells
    }

    for (moverType in 0..1) {
        for (p in 0..1) {
            val path = tryBuild(moverType, p)
            if (path != null) {
                out.writeLineInt(path.size)
                for (cell in path) {
                    out.writePair(cell / n + 1, cell % n + 1)
                }
                out.flush()
                return
            }
        }
    }

    out.writeLineInt(-1)
    out.flush()
}
