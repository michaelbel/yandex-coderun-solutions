import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.util.StringTokenizer
import java.util.Arrays

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    var tokenizer = StringTokenizer("")
    fun next(): String {
        while (!tokenizer.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer.nextToken()
    }

    fun nextInt(): Int = next().toInt()

    val nStr = next()
    if (nStr == "") return
    val n = nStr.toInt()
    val m = nextInt()
    val totalCells = n * m

    val source = IntArray(totalCells)
    val targetPos = IntArray(totalCells + 1) { -1 }
    
    var hasAnyHole = false
    var kCount = 0
    for (i in 0 until totalCells) {
        val v = nextInt()
        source[i] = v
        if (v == 0) hasAnyHole = true else kCount++
    }

    val targetGrid = IntArray(totalCells)
    for (i in 0 until totalCells) {
        val v = nextInt()
        targetGrid[i] = v
        if (v != 0) {
            targetPos[v] = i
        }
    }

    if (!hasAnyHole) {
        if (Arrays.equals(source, targetGrid)) {
            writer.write("0")
        } else {
            writer.write("-1")
        }
        writer.flush()
        return
    }

    val isKept = BooleanArray(totalCells + 1)
    
    val lisTailIndices = IntArray(m + 1)
    val lisParent = IntArray(m + 1)
    val candTgtCols = IntArray(m)
    val candVals = IntArray(m)

    for (r in 0 until n) {
        var listLen = 0
        val rowStart = r * m
        
        for (c in 0 until m) {
            val v = source[rowStart + c]
            if (v != 0) {
                val tPos = targetPos[v]
                val tRow = tPos / m
                if (tRow == r) {
                    val tCol = tPos % m
                    candVals[listLen] = v
                    candTgtCols[listLen] = tCol
                    listLen++
                }
            }
        }

        if (listLen == 0) continue

        var len = 0
        for (i in 0 until listLen) {
            val x = candTgtCols[i]
            
            var left = 0
            var right = len
            while (left < right) {
                val mid = (left + right) / 2
                if (candTgtCols[lisTailIndices[mid]] < x) {
                    left = mid + 1
                } else {
                    right = mid
                }
            }
            
            lisTailIndices[left] = i
            if (left > 0) {
                lisParent[i] = lisTailIndices[left - 1]
            } else {
                lisParent[i] = -1
            }
            
            if (left == len) {
                len++
            }
        }

        if (len > 0) {
            var currIdx = lisTailIndices[len - 1]
            while (currIdx != -1) {
                isKept[candVals[currIdx]] = true
                currIdx = lisParent[currIdx]
            }
        }
    }

    var movedCount = 0
    for (i in 0 until totalCells) {
        val v = source[i]
        if (v != 0 && !isKept[v]) {
            movedCount++
        }
    }

    val parent = IntArray(n) { it }
    val rowHasHole = BooleanArray(n)
    val componentMoves = IntArray(n)
       
    fun find(i: Int): Int {
        if (parent[i] != i) {
            parent[i] = find(parent[i])
        }
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val rootI = find(i)
        val rootJ = find(j)
        if (rootI != rootJ) {
            parent[rootJ] = rootI
            rowHasHole[rootI] = rowHasHole[rootI] || rowHasHole[rootJ]
            componentMoves[rootI] += componentMoves[rootJ]
        }
    }

    for (i in 0 until totalCells) {
        if (source[i] == 0) {
            rowHasHole[i / m] = true
        }
    }

    for (srcIdx in 0 until totalCells) {
        val v = source[srcIdx]
        if (v != 0 && !isKept[v]) {
            val srcRow = srcIdx / m
            val tgtRow = targetPos[v] / m
            
            val rootSrc = find(srcRow)
            val rootTgt = find(tgtRow)
            
            if (rootSrc != rootTgt) {
                union(srcRow, tgtRow)
            }
            
            componentMoves[find(srcRow)]++
        }
    }

    var penalty = 0
    val visitedRoot = BooleanArray(n)

    for (i in 0 until n) {
        val root = find(i)
        if (!visitedRoot[root]) {
            visitedRoot[root] = true
            if (componentMoves[root] > 0 && !rowHasHole[root]) {
                penalty++
            }
        }
    }

    writer.write((movedCount + penalty).toString())
    writer.flush()
    writer.close()
    reader.close()
}
