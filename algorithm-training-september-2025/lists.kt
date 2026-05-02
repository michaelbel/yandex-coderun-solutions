import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private class BaseList(initial: IntArray) {
    var data: IntArray = initial
    var size: Int = initial.size
}

private class ListView(
    val base: BaseList,
    var offset: Int,
    var size: Int,
    val isBase: Boolean
)

private fun parseOneInt(s: String): Int {
    var res = 0
    for (ch in s) {
        res = res * 10 + (ch - '0')
    }
    return res
}

private fun parseTwoInts(s: String): Pair<Int, Int> {
    var i = 0
    var a = 0
    while (i < s.length && s[i] != ',') {
        a = a * 10 + (s[i] - '0')
        i++
    }
    i++ // skip comma
    var b = 0
    while (i < s.length) {
        b = b * 10 + (s[i] - '0')
        i++
    }
    return Pair(a, b)
}

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val writer = BufferedWriter(OutputStreamWriter(System.out))

    val qLine = reader.readLine()
    val q = qLine.trim().toInt()

    val lists = HashMap<String, ListView>(q * 2)
    val out = StringBuilder()

    repeat(q) {
        val line = reader.readLine()

        if (line.startsWith("List ")) {
            // Declaration
            val rest = line.substring(5) // after "List "
            val spaceIdx = rest.indexOf(' ')
            val name = rest.substring(0, spaceIdx)
            val afterName = rest.substring(spaceIdx + 3) // skip " = "

            if (afterName.startsWith("new List(")) {
                // List a = new List(x,y,...)
                val lParen = afterName.indexOf('(')
                val rParen = afterName.lastIndexOf(')')
                val inside = afterName.substring(lParen + 1, rParen)

                val baseArray: IntArray
                if (inside.isEmpty()) {
                    baseArray = IntArray(0)
                } else {
                    val commaCount = inside.count { it == ',' }
                    val temp = IntArray(commaCount + 1)
                    var idx = 0
                    var cur = 0
                    var iPos = 0
                    while (iPos < inside.length) {
                        val ch = inside[iPos]
                        if (ch == ',') {
                            temp[idx++] = cur
                            cur = 0
                        } else {
                            cur = cur * 10 + (ch - '0')
                        }
                        iPos++
                    }
                    temp[idx++] = cur
                    baseArray = IntArray(idx)
                    for (i in 0 until idx) {
                        baseArray[i] = temp[i]
                    }
                }

                val base = BaseList(baseArray)
                val view = ListView(base, 0, base.size, true)
                lists[name] = view
            } else {
                // List b = a.subList(from,to)
                val dotIdx = afterName.indexOf('.')
                val srcName = afterName.substring(0, dotIdx)
                val srcView = lists[srcName]!!

                val lParen = afterName.indexOf('(')
                val rParen = afterName.lastIndexOf(')')
                val inside = afterName.substring(lParen + 1, rParen)
                val (from, to) = parseTwoInts(inside)

                val newOffset = srcView.offset + (from - 1)
                val newSize = to - from + 1

                val view = ListView(srcView.base, newOffset, newSize, false)
                lists[name] = view
            }
        } else {
            // Method call: a.set(...), a.add(...), a.get(...)
            val dotIdx = line.indexOf('.')
            val name = line.substring(0, dotIdx)
            val view = lists[name]!!
            val rest = line.substring(dotIdx + 1)

            when {
                rest.startsWith("set(") -> {
                    val inside = rest.substring(4, rest.length - 1) // i,x
                    val (pos, value) = parseTwoInts(inside)
                    val idx = view.offset + (pos - 1)
                    view.base.data[idx] = value
                }
                rest.startsWith("add(") -> {
                    val inside = rest.substring(4, rest.length - 1) // x
                    val value = parseOneInt(inside)
                    val base = view.base
                    var data = base.data
                    val sz = base.size
                    if (sz == data.size) {
                        val newCap = if (data.size == 0) 1 else data.size * 2
                        val newData = IntArray(newCap)
                        System.arraycopy(data, 0, newData, 0, sz)
                        data = newData
                        base.data = data
                    }
                    data[sz] = value
                    base.size = sz + 1
                    if (view.isBase) {
                        view.size = base.size
                    }
                }
                rest.startsWith("get(") -> {
                    val inside = rest.substring(4, rest.length - 1) // i
                    val pos = parseOneInt(inside)
                    val idx = view.offset + (pos - 1)
                    val value = view.base.data[idx]
                    out.append(value).append('\n')
                }
            }
        }
    }

    writer.write(out.toString())
    writer.flush()
}
